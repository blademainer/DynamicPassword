/**
 * YIXUN_2.0
 */
package com.xiongyingqi.util;

import java.util.Collection;

/**
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-8-22 下午3:50:35
 */
public class CollectionHelper {
	public static boolean notNullAndHasSize(Collection<?> collection) {
		return collection != null && collection.size() > 0;
	}
}
