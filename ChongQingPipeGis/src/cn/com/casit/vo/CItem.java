package cn.com.casit.vo;

public class CItem {
	private int ID;
	private String Value = "";

	public CItem() {
		ID = 0;
		Value = "";
	}

	public CItem(int _ID, String _Value) {
		ID = _ID;
		Value = _Value;
	}

	@Override
	public String toString() {
		// ΪʲôҪ��дtoString()�أ���Ϊ����������ʾ���ݵ�ʱ����������������Ķ������ַ���������£�ֱ�Ӿ�ʹ�ö���.toString()
		// TODO Auto-generated method stub
		return Value;
	}

	public int GetID() {
		return ID;
	}

	public String GetValue() {
		return Value;
	}
}