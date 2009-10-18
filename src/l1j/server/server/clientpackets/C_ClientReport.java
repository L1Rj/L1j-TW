package l1j.server.server.clientpackets;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

/**
 * 用戶端要求線上公告
 * 要發送的線上公告請於/data/toall.txt設定
 * @author a0917009769
 */
public class C_ClientReport extends ClientBasePacket {
		
		private static final String C_CLIENT_REPORT = "[C] C_ClientReport";
		private static Logger _log = Logger.getLogger(C_ClientReport.class.getName());
		private ArrayList _ToAll;
		public C_ClientReport(byte abyte0[], ClientThread clientthread)
		throws Exception {
			
			super(abyte0);
			
			L1PcInstance pc = clientthread.getActiveChar();
			
			_ToAll = new ArrayList();
			loadAnnouncements();
			String message = "";
			for (int i = 0; i < _ToAll.size(); i++) {
				message = (new StringBuilder()).append(message).append(
						_ToAll.get(i).toString()).append("\n").toString();
			}
			
			pc.sendPackets(new S_SystemMessage(message));
			System.out.println("report");
		}
		private void loadAnnouncements() {
			_ToAll.clear();
			File file = new File("data/toall.txt");
			if (file.exists()) {
				readFromDisk(file);
			}
		}
		private void readFromDisk(File file) {
			LineNumberReader lnr = null;
			try {
				String line = null;
				lnr = new LineNumberReader(new FileReader(file));
				do {
					if ((line = lnr.readLine()) == null) {
						break;
					}
					StringTokenizer st = new StringTokenizer(line, "\n\r");
					if (st.hasMoreTokens()) {
						String announcement = st.nextToken();
						_ToAll.add(announcement);
					} else {
						_ToAll.add(" ");
					}
				} while (true);
			} catch (Exception e) {
			}
		}
		
		@Override
		public String getType() {
			return C_CLIENT_REPORT;
		}

}
