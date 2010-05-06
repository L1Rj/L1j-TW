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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.l1j.telnet.command.TelnetCommandExecutor;
import net.l1j.telnet.command.TelnetCommandResult;

public class TelnetThread extends Thread {
	private static final Logger _log = Logger.getLogger(TelnetThread.class.getName());

	private Socket _cSocket;

	private BufferedReader _reader;
	private BufferedWriter _writer;

	public TelnetThread(Socket client) throws IOException {
		_cSocket = client;

		_reader = new BufferedReader(new InputStreamReader(_cSocket.getInputStream()));
		_writer = new BufferedWriter(new OutputStreamWriter(_cSocket.getOutputStream()));

		start();
	}

	@Override
	public void run() {
		String userCommand = "";
		try {
			while (null != (userCommand = _reader.readLine())) {
				TelnetCommandResult result = TelnetCommandExecutor.getInstance().execute(userCommand);
				_writer.write(result.getCode() + " " + result.getCodeMessage() + "\r\n");
				_writer.write(result.getResult() + "\r\n");
				_writer.flush();
			}

			if (!_cSocket.isClosed()) {
				_writer.flush();
				_cSocket.close();
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
