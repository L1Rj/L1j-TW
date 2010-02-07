/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.l1j.server.utils.StreamUtil;

public class BackUpLog {
	/**
	 * String that points to root directory for backups
	 */
	private static String backupDir = "log/backup";

	/**
	 * String that represents date format for backup files
	 */
	private static String backupDateFormat = "yyyy-MM-dd HH-mm-ss";

	public static void backup() {
		File file = new File("log/Server.db");
		// Linux systems doesn't provide file creation time, so we have to hope that log files
		// were not modified manually after server starup
		// We can use here Windowns-only solution but that suck :(
		if (isFileOlder(file, ManagementFactory.getRuntimeMXBean().getStartTime())) {
			File backupRoot = new File(backupDir);
			if (!backupRoot.exists() && !backupRoot.mkdirs()) {
				throw new AppenderInitializationError("Can't create backup dir for backup storage");
			}

			SimpleDateFormat df;
			try {
				df = new SimpleDateFormat(backupDateFormat);
			} catch (Exception e) {
				throw new AppenderInitializationError("Invalid date formate for backup files: " + backupDateFormat, e);
			}
			String date = df.format(new Date(file.lastModified()));

			File zipFile = new File(backupRoot, file.getName() + "." + date + ".zip");

			ZipOutputStream zos = null;
			FileInputStream fis = null;
			try {
				zos = new ZipOutputStream(new FileOutputStream(zipFile));
				ZipEntry entry = new ZipEntry(file.getName());
				entry.setMethod(ZipEntry.DEFLATED);
				entry.setCrc(checksumCRC32(file));
				zos.putNextEntry(entry);
				fis = openInputStream(file);

				byte[] buffer = new byte[1024];
				int readed;
				while ((readed = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, readed);
				}
			} catch (Exception e) {
				throw new AppenderInitializationError("Can't create zip file", e);
			} finally {
				StreamUtil.close(zos);
				StreamUtil.close(fis);
			}

			if (!file.delete()) {
				throw new AppenderInitializationError("Can't delete old log file " + file.getAbsolutePath());
			}
		}
	}

	/**
	 * Tests if the specified <code>File</code> is older than the specified time reference.
	 * 
	 * @param file the <code>File</code> of which the modification date must be compared, must not be <code>null</code>
	 * @param timeMillis the time reference measured in milliseconds since the epoch (00:00:00 GMT, January 1, 1970)
	 * @return true if the <code>File</code> exists and has been modified before the given time reference.
	 * @throws IllegalArgumentException if the file is <code>null</code>
	 */
	public static boolean isFileOlder(File file, long timeMillis) {
		if (file == null) {
			throw new IllegalArgumentException("No specified file");
		}
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() < timeMillis;
	}

	/**
	 * Computes the checksum of a file using the CRC32 checksum routine. The value of the checksum is returned.
	 * 
	 * @param file the file to checksum, must not be <code>null</code>
	 * @return the checksum value
	 * @throws NullPointerException if the file or checksum is <code>null</code>
	 * @throws IllegalArgumentException if the file is a directory
	 * @throws IOException if an IO error occurs reading the file
	 * @since Commons IO 1.3
	 */
	public static long checksumCRC32(File file) throws IOException {
		CRC32 crc = new CRC32();
		checksum(file, crc);
		return crc.getValue();
	}

	/**
	 * Computes the checksum of a file using the specified checksum object.
	 * Multiple files may be checked using one <code>Checksum</code> instance if desired simply by reusing the same checksum object.
	 * <br><br>
	 * For example:
	 * <pre>
	 * long csum = FileUtils.checksum(file, new CRC32()).getValue();
	 * </pre>
	 * 
	 * @param file the file to checksum, must not be <code>null</code>
	 * @param checksum the checksum object to be used, must not be <code>null</code>
	 * @return the checksum specified, updated with the content of the file
	 * @throws NullPointerException if the file or checksum is <code>null</code>
	 * @throws IllegalArgumentException if the file is a directory
	 * @throws IOException if an IO error occurs reading the file
	 * @since Commons IO 1.3
	 */
	public static Checksum checksum(File file, Checksum checksum) throws IOException {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Checksums can't be computed on directories");
		}
		InputStream in = null;
		try {
			in = new CheckedInputStream(new FileInputStream(file), checksum);
			copy(in, new NullOutputStream());
		} finally {
			StreamUtil.close(in);
		}
		return checksum;
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 * Large streams (over 2GB) will return a bytes copied value of
	 * <code>-1</code> after the copy has completed since the correct number of
	 * bytes cannot be returned as an int. For large streams use the
	 * <code>copyLarge(InputStream, OutputStream)</code> method.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @throws ArithmeticException if the byte count is too large
	 * @since Commons IO 1.1
	 */
	public static int copy(InputStream input, OutputStream output) throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	/**
	 * Copy bytes from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.3
	 */
	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Opens a {@link FileInputStream} for the specified file, providing better
	 * error messages than simply calling <code>new FileInputStream(file)</code>
	 * .
	 * <p>
	 * At the end of the method either the stream will be successfully opened,
	 * or an exception will have been thrown.
	 * <p>
	 * An exception is thrown if the file does not exist. An exception is thrown
	 * if the file object exists but is a directory. An exception is thrown if
	 * the file exists but cannot be read.
	 * 
	 * @param file the file to open for input, must not be <code>null</code>
	 * @return a new {@link FileInputStream} for the specified file
	 * @throws FileNotFoundException if the file does not exist
	 * @throws IOException if the file object is a directory
	 * @throws IOException if the file cannot be read
	 * @since Commons IO 1.3
	 */
	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file + "' does not exist");
		}
		return new FileInputStream(file);
	}

	/**
	 * This OutputStream writes all data to the famous <b>/dev/null</b>.
	 * <p>
	 * This output stream has no destination (file/socket etc.) and all bytes
	 * written to it are ignored and lost.
	 * 
	 * @author Jeremias Maerki
	 * @version $Id: NullOutputStream.java 659817 2008-05-24 13:23:10Z niallp $
	 */
	static class NullOutputStream extends OutputStream {
		/**
		 * A singleton.
		 */
		public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

		/**
		 * Does nothing - output to <code>/dev/null</code>.
		 * 
		 * @param b The bytes to write
		 * @param off The start offset
		 * @param len The number of bytes to write
		 */
		@Override
		public void write(byte[] b, int off, int len) {
			// to /dev/null
		}

		/**
		 * Does nothing - output to <code>/dev/null</code>.
		 * 
		 * @param b The byte to write
		 */
		@Override
		public void write(int b) {
			// to /dev/null
		}

		/**
		 * Does nothing - output to <code>/dev/null</code>.
		 * 
		 * @param b The bytes to write
		 * @throws IOException never
		 */
		@Override
		public void write(byte[] b) throws IOException {
			// to /dev/null
		}
	}

	static class AppenderInitializationError extends Error {
		private static final long serialVersionUID = 1L;

		/** Creates new Error */
		public AppenderInitializationError() {
		}

		/**
		 * Creates new error
		 * 
		 * @param message error description
		 */
		public AppenderInitializationError(String message) {
			super(message);
		}

		/**
		 * Creates new error
		 * 
		 * @param message error description
		 * @param cause reason of this error
		 */
		public AppenderInitializationError(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * Creates new error
		 * 
		 * @param cause reason of this error
		 */
		public AppenderInitializationError(Throwable cause) {
			super(cause);
		}
	}
}
