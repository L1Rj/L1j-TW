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
package net.l1j.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author lord_rex This class is for get/log system informations.
 */
public class InfoUtil {
	private static Logger _log = Logger.getLogger(InfoUtil.class.getName());

	public static String[] getMemoryInfo() {
		double max = Runtime.getRuntime().maxMemory() / 1024; // maxMemory is the upper limit the jvm can use
		double allocated = Runtime.getRuntime().totalMemory() / 1024; // totalMemory the size of the current allocation pool
		double nonAllocated = max - allocated; // non allocated memory till jvm limit
		double cached = Runtime.getRuntime().freeMemory() / 1024; // freeMemory the unused memory in the allocation pool
		double used = allocated - cached; // really used memory
		double useable = max - used; // allocated, but non-used and non-allocated memory
		DecimalFormat df = new DecimalFormat(" (0.0000'%')");
		DecimalFormat df2 = new DecimalFormat(" # 'KB'");

		return new String[] {
		    "+----",
			"| 記憶體使用資訊於 " + getRealTime().toString() + ":",
			"|    |",
			"| 允許使用記憶體:" + df2.format(max),
			"|    |= 動態分配記憶體:" + df2.format(allocated) + df.format(allocated / max * 100),
			"|    |= 非動態記憶體:" + df2.format(nonAllocated) + df.format(nonAllocated / max * 100),
			"| 動態分配記憶體:" + df2.format(allocated),
			"|    |= 使用記憶體:" + df2.format(used) + df.format(used / max * 100),
			"|    |= 未使用 (快取) 記憶體:" + df2.format(cached) + df.format(cached / max * 100),
			"| 可用記憶體:" + df2.format(useable) + df.format(useable / max * 100),
			"+----"
		};
	}

	public static String[] getCPUInfo() {
		return new String[] {
		    "使用的中央處理器: " + Runtime.getRuntime().availableProcessors(),
			"處理器識別碼: " + System.getenv("PROCESSOR_IDENTIFIER"),
			"=================================================",
		};
	}

	public static String[] getOSInfo() {
		return new String[] {
		    "作業系統: " + System.getProperty("os.name") + " Build: " + System.getProperty("os.version"),
			"作業系統位元版本: " + System.getProperty("os.arch"),
			"=================================================",
		};
	}

	public static String[] getJREInfo() {
		return new String[] {
			"Java Platform 資訊",
			"Java Runtime 名稱: " + System.getProperty("java.runtime.name"),
			"Java 版本: " + System.getProperty("java.version"),
			"Java Class 版本: " + System.getProperty("java.class.version"),
			"=================================================",
		};
	}

	public static String[] getJVMInfo() {
		return new String[] {
			"JAVA虛擬機器資訊 (JVM)",
			"JVM 名稱: " + System.getProperty("java.vm.name"),
			"JVM 安裝目錄: " + System.getProperty("java.home"),
			"JVM 版本: " + System.getProperty("java.vm.version"),
			"JVM 製作公司: " + System.getProperty("java.vm.vendor"),
			"JVM 模式: " + System.getProperty("java.vm.info"),
			"=================================================",
		};
	}

	public static String getRealTime() {
		SimpleDateFormat String = new SimpleDateFormat("H:mm:ss");

		return String.format(new Date());
	}

	public static void printMemoryInfo() {
		for (String line : getMemoryInfo()) {
			_log.info(line);
		}
	}

	public static void printCPUInfo() {
		for (String line : getCPUInfo()) {
			_log.info(line);
		}
	}

	public static void printOSInfo() {
		for (String line : getOSInfo()) {
			_log.info(line);
		}
	}

	public static void printJREInfo() {
		for (String line : getJREInfo()) {
			_log.info(line);
		}
	}

	public static void printJVMInfo() {
		for (String line : getJVMInfo()) {
			_log.info(line);
		}
	}

	public static void printRealTime() {
		_log.info(getRealTime().toString());
	}

	public static void printAllInfos() {
		printOSInfo();
		printCPUInfo();
		printJREInfo();
		printJVMInfo();
		printMemoryInfo();
	}
}