package specification;

// 对数据是否符合规范的判断
// 使用了正则表达式

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataValid {

	// 判断密码是否符合规范——6位以上字符或数字，必须包含至少一个字符
	public static boolean isPasswordValid(String password) {
		boolean rtn = false;
		int len = password.length();
		if (6 > len) {
			rtn = false;
		} else {
			Pattern pattern = Pattern.compile("\\d{" + len + "}");
			Matcher isNum = pattern.matcher(password);
			if (!isNum.matches()) {
				rtn = true;
				;
			} else {
				rtn = false;
			}
		}
		return rtn;
	}

	// 判断电话号码是否符合规范——11位数字，开头是139、138、137、136、135、134、159、158、157、152、151、150、188、187、130、131、132、155、156、186、185、
	// 133、153、189、180、184
	public static boolean isPhoneNumberValid(String phone) {
		boolean rtn = false;
		Pattern pattern = Pattern
				.compile("(139|138|137|136|135|134|159|158|157|152|151|150|188|187|130|131|132|155|156|186|185|133|153|189|180|184)([\\d]{8})");
		Matcher isValid = pattern.matcher(phone);
		if (isValid.matches()) {
			rtn = true;
		} else {
			rtn = false;
		}
		return rtn;
	}
}
