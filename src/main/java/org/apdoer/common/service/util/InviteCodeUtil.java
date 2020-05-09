package org.apdoer.common.service.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <b>基于baseStr,将用户的uid转成34进制的数字做为用户的唯一邀请码，假设按{@link #INVITE_CODE_MIN_LENGTH}位长度算的话，最多可支持
 * 34^{@link #INVITE_CODE_MIN_LENGTH}
 * 个用户userId，userId超过此范围时，邀请码将自动升级为多于{@link #INVITE_CODE_MIN_LENGTH}位</b></br>
 * <b>{@link #mapToCode(long userId)}
 * 将userId+{@link #INVITE_CODE_OFFSET}进行10进制到34进制的转换,得到的34进制数如果不足{@link #INVITE_CODE_MIN_LENGTH}位时，将在左侧用0补位。当转换结果超过{@link #INVITE_CODE_MIN_LENGTH}位直接返回</b></br>
 * <b>{@link #demapToUserId(String inviteCode)}
 * 将邀请码进行34进制到10进制转换，得到的值-{@link #INVITE_CODE_OFFSET}即为用户的userId</b></br>
 * 
 * @author William
 */
public class InviteCodeUtil {
	private static final int	INVITE_CODE_MIN_LENGTH		= 6;
	private static final int	INVITE_CODE_OFFSET			= 46104;

	/**
	 * 这个数组非常重要，不允许变更，当有变更时，将导致之前发出的邀请码无法反向解析 0必须是第一位，因为不足时会在前面补0
	 */
	private static String		baseStr						= "0,V,H,F,2,Q,D,3,X,4,G,5,W,B,6,L,7,S,M,8,U,Z,T,N,9,K,A,C,E,Y,J,P,1,R";
	/**
	 * 邀请码生成的basestr 总共34个
	 */
	private static String[]		base4Str					= baseStr.split( "," );
	private static String		str							= Arrays.stream( base4Str ).collect( Collectors.joining( "" ) );

	/**
	 * 邀请码验证正则
	 */
	private static final String	INVITE_CODE_FORMART_REGEX	= "^[0-9A-Z]{" + INVITE_CODE_MIN_LENGTH + ",}$";

	/**
	 * userId加{@link #INVITE_CODE_OFFSET}映射成最少是{@link #INVITE_CODE_MIN_LENGTH}位的邀请码
	 * 
	 * @param userId
	 * @return
	 */
	public static String mapToCode(long userId) {
		long size = base4Str.length;
		if (0 > userId) {
			return "";
		}
		long remain = userId + INVITE_CODE_OFFSET;
		String res = "";
		while (remain >= size) {
			int index = (int) (remain % size);
			res = base4Str[ index ] + res;
			remain = remain / size;
		}
		res = base4Str[ (int) remain ] + res;
		if (res.length() < INVITE_CODE_MIN_LENGTH) {
			// 左边补0
			int prefixSize = INVITE_CODE_MIN_LENGTH - res.length();
			for (int i = 0; i < prefixSize; i++) {
				res = "0" + res;
			}
		}
		return res;
	}

	/**
	 * 邀请码反向解析为userId
	 * 
	 * @param inviteCode
	 * @return
	 */
	public static long demapToUserId(final String inviteCode) {
		if (StringUtils.isBlank( inviteCode )) {
			return -1;
		}
		// 全部转大写
		final String inviteCodeUp = inviteCode.toUpperCase();
		int size = base4Str.length;
		long userId = 0;
		for (int i = inviteCodeUp.length() - 1; i >= 0; i--) {
			char s = inviteCodeUp.charAt( i );
			int j = str.indexOf( s );
			userId = userId + (long) (j * (Math.pow( size, (inviteCodeUp.length() - i - 1) )));
		}
		return userId - INVITE_CODE_OFFSET;
	}

	/**
	 * 验证邀请码格式
	 * 
	 * @param inviteCode
	 *            邀请码
	 * @return 是否符合格式
	 */
	public static boolean validateInviteCode(final String inviteCode) {
		return Pattern.matches( INVITE_CODE_FORMART_REGEX, inviteCode );
	}

}
