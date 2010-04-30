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
package net.l1j.util;

/**
 * String utilities optimized for the best performance.
 * 
 * <h1>How to Use It</h1> <h2>concat() or append()</h2> If concatenating strings
 * in single call, use StringUtil.concat(), otherwise use StringUtil.append()
 * and its variants. <h2>Minimum Calls</h2> Bad:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;text 1&quot;, String.valueOf(npcId));
 * StringUtil.append(&quot;text 2&quot;);
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;text 1&quot;, String.valueOf(npcId), &quot;text 2&quot;);
 * </pre>
 * 
 * Why?<br/>
 * Because the less calls you do, the less memory re-allocations have to be done
 * so the whole text fits into the memory and less array copy tasks has to be
 * performed. So if using less calls, less memory is used and string
 * concatenation is faster. <h2>Size Hints for Loops</h2> Bad:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;header start&quot;, someText, &quot;header end&quot;);
 * for (int i = 0; i &lt; 50; i++)
 * {
 * 	StringUtil.append(sbString, &quot;text 1&quot;, stringArray[i], &quot;text 2&quot;);
 * }
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final StringBuilder sbString = StringUtil.startAppend(1300, &quot;header start&quot;, someText, &quot;header end&quot;);
 * for (int i = 0; i &lt; 50; i++)
 * {
 * 	StringUtil.append(sbString, &quot;text 1&quot;, stringArray[i], &quot;text 2&quot;);
 * }
 * </pre>
 * 
 * Why?<br/>
 * When using StringUtil.append(), memory is only allocated to fit in the
 * strings in method argument. So on each loop new memory for the string has to
 * be allocated and old string has to be copied to the new string. With size
 * hint, even if the size hint is above the needed memory, memory is saved
 * because new memory has not to be allocated on each cycle. Also it is much
 * faster if no string copy tasks has to be performed. So if concatenating
 * strings in a loop, count approximately the size and set it as the hint for
 * the string builder size. It's better to make the size hint little bit larger
 * rather than smaller.<br/>
 * In case there is no text appended before the cycle, just use <code>new
 * StringBuilder(1300)</code>. <h2>Concatenation and Constants</h2> Bad:
 * 
 * <pre>
 * StringUtil.concat(&quot;text 1 &quot;, &quot;text 2&quot;, String.valueOf(npcId));
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * StringUtil.concat(&quot;text 1 &quot; + &quot;text 2&quot;, String.valueOf(npcId));
 * </pre>
 * 
 * or
 * 
 * <pre>
 * StringUtil.concat(&quot;text 1 text 2&quot;, String.valueOf(npcId));
 * </pre>
 * 
 * Why?<br/>
 * It saves some cycles when determining size of memory that needs to be
 * allocated because less strings are passed to concat() method. But do not use
 * + for concatenation of non-constant strings, that degrades performance and
 * makes extra memory allocations needed. <h2>Concatenation and Constant
 * Variables</h2> Bad:
 * 
 * <pre>
 * String glue = &quot;some glue&quot;;
 * StringUtil.concat(&quot;text 1&quot;, glue, &quot;text 2&quot;, glue, String.valueOf(npcId));
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final String glue = &quot;some glue&quot;;
 * StringUtil.concat(&quot;text 1&quot; + glue + &quot;text2&quot; + glue, String.valueOf(npcId));
 * </pre>
 * 
 * Why? Because when using <code>final</code> keyword, the <code>glue</code> is
 * marked as constant string and compiler treats it as a constant string so it
 * is able to create string "text1some gluetext2some glue" during the
 * compilation. But this only works in case the value is known at compilation
 * time, so this cannot be used for cases like
 * <code>final String objectIdString =
 * String.valueOf(getObjectId)</code>. <h2>StringBuilder Reuse</h2> Bad:
 * 
 * <pre>
 * final StringBuilder sbString1 = new StringBuilder();
 * StringUtil.append(sbString1, &quot;text 1&quot;, String.valueOf(npcId), &quot;text 2&quot;);
 * ... // output of sbString1, it is no more needed
 * final StringBuilder sbString2 = new StringBuilder();
 * StringUtil.append(sbString2, &quot;text 3&quot;, String.valueOf(npcId), &quot;text 4&quot;);
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;text 1&quot;, String.valueOf(npcId), &quot;text 2&quot;);
 * ... // output of sbString, it is no more needed
 * sbString.setLength(0);
 * StringUtil.append(sbString, &quot;text 3&quot;, String.valueOf(npcId), &quot;text 4&quot;);
 * </pre>
 * 
 * Why?</br> In first case, new memory has to be allocated for the second
 * string. In second case already allocated memory is reused, but only in case
 * the new string is not longer than the previously allocated string. Anyway,
 * the second way is better because the string either fits in the memory and
 * some memory is saved, or it does not fit in the memory, and in that case it
 * works as in the first case. <h2>Primitives to Strings</h2> To convert
 * primitives to string, use String.valueOf(). <h2>How much faster is it?</h2>
 * Here are some results of my tests. Count is number of strings concatenated.
 * Don't take the numbers as 100% true as the numbers are affected by other
 * programs running on my computer at the same time. Anyway, from the results it
 * is obvious that using StringBuilder with predefined size is the fastest (and
 * also most memory efficient) solution. It is about 5 times faster when
 * concatenating 7 strings, compared to TextBuilder. Also, with more strings
 * concatenated, the difference between StringBuilder and TextBuilder gets
 * larger. In code, there are many cases, where there are concatenated 50+
 * strings so the time saving is even greater.
 * 
 * <pre>
 * Count: 2
 * TextBuilder: 1893
 * TextBuilder with size: 1703
 * String: 1033
 * StringBuilder: 993
 * StringBuilder with size: 1024
 * Count: 3
 * TextBuilder: 1973
 * TextBuilder with size: 1872
 * String: 2583
 * StringBuilder: 1633
 * StringBuilder with size: 1156
 * Count: 4
 * TextBuilder: 2188
 * TextBuilder with size: 2229
 * String: 4207
 * StringBuilder: 1816
 * StringBuilder with size: 1444
 * Count: 5
 * TextBuilder: 9185
 * TextBuilder with size: 9464
 * String: 6937
 * StringBuilder: 2745
 * StringBuilder with size: 1882
 * Count: 6
 * TextBuilder: 9785
 * TextBuilder with size: 10082
 * String: 9471
 * StringBuilder: 2889
 * StringBuilder with size: 1857
 * Count: 7
 * TextBuilder: 10169
 * TextBuilder with size: 10528
 * String: 12746
 * StringBuilder: 3081
 * StringBuilder with size: 2139
 * </pre>
 * 
 * @author fordfrog
 */
public final class StringUtil {
	/**
	 * Concatenates strings.
	 * 
	 * @param strings strings to be concatenated
	 * @return concatenated string
	 * @see StringUtil
	 */
	public static String concat(final String... strings) {
		final StringBuilder sbString = new StringBuilder(getLength(strings));

		for (final String string : strings) {
			sbString.append(string);
		}
		return sbString.toString();
	}

	/**
	 * Creates new string builder with size initializated to
	 * <code>sizeHint</code>, unless total length of strings is greater than
	 * <code>sizeHint</code>.
	 * 
	 * @param sizeHint hint for string builder size allocation
	 * @param strings strings to be appended
	 * @return created string builder
	 * @see StringUtil
	 */
	public static StringBuilder startAppend(final int sizeHint, final String... strings) {
		final int length = getLength(strings);
		final StringBuilder sbString = new StringBuilder(sizeHint > length ? sizeHint : length);

		for (final String string : strings) {
			sbString.append(string);
		}
		return sbString;
	}

	/**
	 * Appends strings to existing string builder.
	 * 
	 * @param sbString string builder
	 * @param strings strings to be appended
	 * @see StringUtil
	 */
	public static void append(final StringBuilder sbString, final String... strings) {
		sbString.ensureCapacity(sbString.length() + getLength(strings));

		for (final String string : strings) {
			sbString.append(string);
		}
	}

	/**
	 * Counts total length of all the strings.
	 * 
	 * @param strings array of strings
	 * @return total length of all the strings
	 */
	private static int getLength(final String[] strings) {
		int length = 0;

		for (final String string : strings) {
			if (string == null) {
				length += 4;
			} else {
				length += string.length();
			}
		}
		return length;
	}
}
