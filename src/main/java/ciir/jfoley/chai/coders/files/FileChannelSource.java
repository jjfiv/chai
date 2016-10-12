package ciir.jfoley.chai.coders.files;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.file.StandardOpenOption.READ;

/**
 * @author jfoley.
 */
public class FileChannelSource implements DataSource {
  private final FileChannel channel;
  private final long size;

  public FileChannelSource(String path) throws IOException {
    this.channel = FileChannel.open(new File(path).toPath(), READ);
    this.size = channel.size();
  }

  @Override
  public long size() throws IOException {
    return size;
  }

  /**
   * Read size bytes from position or crash.
   * @param position the position within the file.
   * @param size the number of bytes to read.
   * @return a heap-allocated ByteBuffer containing the data needed.
   * @throws IOException if any trouble occurs while reading.
   */
  @Override
  public ByteBuffer read(long position, int size) throws IOException {
    ByteBuffer dat = ByteBuffer.allocate(size);
    int amt = channel.read(dat, position);
    if(amt != size) throw new IOException("Couldn't read "+size+" bytes at position: "+position);
    return dat;
  }

  /**
   * Return a subset of the current source as its own source.
   * @param position the starting position in the current source of the new source (will become 0)
   * @param size the size of the new source.
   * @return a new DataSource, probably of type {:link OffsetDataSource}
   * @throws IOException
   */
  @Override
  public DataSource view(long position, long size) throws IOException {
    if(position + size > size()) throw new IllegalArgumentException("Can't request that much!");

    return OffsetDataSource.FromPositionSize(this, position, size);
  }

  @Override
  public int read(long position) throws IOException {
    if(position < 0 || position >= size()) {
      return -1;
    }
    ByteBuffer read = read(position, 1);
    return (read.array()[0] & 0xff);
  }

  @Override
  public void close() throws IOException {
    channel.close();
  }
}
