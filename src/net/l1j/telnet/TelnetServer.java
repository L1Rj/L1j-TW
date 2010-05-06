/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.l1j.telnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import net.l1j.Config;

public class TelnetServer extends Thread {
	protected static final Logger _log = Logger.getLogger(TelnetServer.class.getName());

	private ServerSocket _telnetServerSocket;

	@Override
	public void run() {
		this.setPriority(Thread.MAX_PRIORITY);

		while (true) {
			try {
				Socket connection = _telnetServerSocket.accept();

				new TelnetThread(connection);

				if (this.isInterrupted()) {
					try {
						_telnetServerSocket.close();
					} catch (IOException io) {
						io.printStackTrace();
					}
					break;
				}
			} catch (IOException e) {
				if (this.isInterrupted()) {
					try {
						_telnetServerSocket.close();
					} catch (IOException io) {
						io.printStackTrace();
					}
					break;
				}
			}
		}
	}

	public TelnetServer() throws IOException {
		super("TelnetServer");

		_telnetServerSocket = new ServerSocket(Config.TELNET_SERVER_PORT);
	}
}
