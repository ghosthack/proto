package turismo.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class SimpleMultipartInputStream extends InputStream {

  public SimpleMultipartInputStream(InputStream is, String characterEncoding, String boundary) throws UnsupportedEncodingException {
    this.is = is;
    boundaryBytesLength = boundary.getBytes(characterEncoding).length;
    endBoundaryMatcher = new Matcher((CRLF + boundary + BOUNDARY_END).getBytes(characterEncoding));
    doubleCrlf = new Matcher((CRLF + CRLF).getBytes(characterEncoding));
    linkedList = new LinkedList<Integer>();
    startBoundarySkip = true;
    contentBoundarySkip = true;
    streamState = StreamState.SKIPPING;
    dequeue = 0;
  }

  private enum StreamState {
    SKIPPING,
    ENQUEUEING,
    DEQUEUEING,
    FINISHING,
  }

  private enum MatcherState {
    COMPLETE_MATCHING, // drop matching elements
    IS_MATCHING, // add current to linkedList
    STOPPED_MATCHING, // add current to linkedList, dequeue N, all (current linkedList plus read value)
    RESTARTED_MATCHING, // add current to linkedList, dequeue N-1, all but current (read value is part of a new block)
    NOT_MATCHING, // do not interact
  }

  private class Matcher {
    public byte[] bytes;
    public int current;
    public int last;

    public Matcher(byte[] bytes) {
      this.bytes = bytes;
    }

    public MatcherState evalNext(byte b) {
      if(b == bytes[current]) {
        current++;
        if(current == bytes.length) {
          current = 0;
          return MatcherState.COMPLETE_MATCHING;
        } else {
          return MatcherState.IS_MATCHING;
        }
      } else {
        if(current == 0) {
          return MatcherState.NOT_MATCHING;
        } else {
          last = current;
          if(b == bytes[0]) {
            last--;
            current = 1;
            return MatcherState.RESTARTED_MATCHING;
          } else {
            last++;
            current = 0;
            return MatcherState.STOPPED_MATCHING;
          }
        }
      }
    }
  }

  private void read1() throws IOException {
    nextInt = is.read();
    if(nextInt == -1) {
      throw new IOException("Stream ended prematurely");
    }
    nextByte = (byte) nextInt;
  }

  @Override
  public int read() throws IOException {
    for(;;) {
      if(streamState == StreamState.SKIPPING) {
        skip();
      } else
      if(streamState == StreamState.ENQUEUEING) {
        if (enqueue()) return nextInt;
      } else
      if(streamState == StreamState.DEQUEUEING) {
        if(dequeue > 0) {
            return dequeue();
        } else {
          streamState = StreamState.ENQUEUEING;
        }
      } else
      if(streamState == StreamState.FINISHING) {
        Integer head = linkedList.poll();
        if(head == null) {
          return -1;
        } else {
          return head;
        }
      }
    }
  }

    private int dequeue() {
        dequeue--;
        return linkedList.poll();
    }

    private boolean enqueue() throws IOException {
    read1();
    MatcherState matcherState = endBoundaryMatcher.evalNext(nextByte);
    switch (matcherState) {
      case NOT_MATCHING:
        return true;
      case STOPPED_MATCHING:
      case RESTARTED_MATCHING:
        streamState = StreamState.DEQUEUEING;
        linkedList.offer(nextInt);
        dequeue = endBoundaryMatcher.last;
        break;
      case IS_MATCHING:
        linkedList.offer(nextInt);
        break;
      case COMPLETE_MATCHING:
        removeLast(endBoundaryMatcher.bytes.length-1); //last element was not queued
        streamState = StreamState.FINISHING;
        break;
    }
    return false;
  }

  private void skip() throws IOException {
    if(startBoundarySkip) {
      startBoundarySkip = false;
      if(is.skip(boundaryBytesLength) != boundaryBytesLength) {
        throw new IOException("Stream format unexpected");
      }
    } else
    if(contentBoundarySkip) {
      read1();
      switch (doubleCrlf.evalNext(nextByte)) {
        case COMPLETE_MATCHING:
          contentBoundarySkip = false;
          streamState = StreamState.ENQUEUEING;
          break;
      }
    }
  }

  private void removeLast(int count) {
    for(int i = 0; i < count; i++) {
      linkedList.removeLast();
    }
  }

  private static final String BOUNDARY_END = "--"; // Final boundary ends with an extra -- (instead a CRLF)
  private static final String CRLF = "\r\n"; // 0x0D 0x0A
  private InputStream is;
  private Matcher endBoundaryMatcher;
  private Matcher doubleCrlf;
  private LinkedList<Integer> linkedList;
  private boolean startBoundarySkip;
  private boolean contentBoundarySkip;
  private int boundaryBytesLength;
  private StreamState streamState;
  private int dequeue;
  private byte nextByte;
  private int nextInt;


}