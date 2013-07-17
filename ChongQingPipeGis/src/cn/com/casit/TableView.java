package cn.com.casit;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


@SuppressLint({ "ViewConstructor", "ViewConstructor" })
public class TableView extends LinearLayout {

	private static LayoutParams FILL_FILL_LAYOUTPARAMS = new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1);
	private static LayoutParams WAP_WAP_LAYOUTPARAMS = new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	private static Paint BLACK_PAINT = new Paint();
	private static Paint WHITE_PAINT = new Paint();
	static {
		WHITE_PAINT.setColor(Color.WHITE);
		BLACK_PAINT.setColor(Color.BLACK);
	}

	private CAdapter cAdapter;

	/** ����ռ�. */
	private LinearLayout titleLayout;
	private String[] title;

	private ListView listView;
	/** ����. */
	private List<String[]> data;

	/** �п�����. */
	private int[] itemWidth;

	/** ��ǰѡ����. */
	private int selectedPosition = -1;
	/** �Զ��п���. */
	private int autoWidthIndex = -1;

	private AdapterView.OnItemClickListener onItemClickListener;

	/** �б�����ɫ. */
	private int[] rowsBackgroundColor;
	/** ѡ���б�����ɫ. */
	private int selectedBackgroundColor = Color.argb(200, 255, 210, 102);
	/** ���ⱳ����ɫ. */
	private int titleBackgroundColor;
	/** ����������ɫ. */
	private int titleTextColor = Color.argb(255, 100, 100, 100);
	/** ����������ɫ. */
	private int contentTextColor = Color.argb(255, 100, 100, 100);
	/** ���������С. */
	private float titleTextSize = 0;
	/** ���������С. */
	private float contentTextSize = 0;

	/**
	 * ��ʼ��������ListView
	 * 
	 * @param context
	 *            ����������
	 * @param title
	 *            ��������
	 * @param data
	 *            �����б�
	 */
	public TableView(Context context, String[] title, List<String[]> data) {
		super(context);

		this.title = title;
		this.data = data;

		// �趨���򲼾�
		setOrientation(VERTICAL);
		// �趨����Ϊ��ɫ
		setBackgroundColor(Color.WHITE);

		// Ԥ���趨��ÿ�еĿ�
		this.itemWidth = new int[title.length];
		autoWidthIndex = this.itemWidth.length - 1;
		// �����п�
		calcColumnWidth();

		// ���titleλ��
		titleLayout = new LinearLayout(getContext());
		titleLayout.setBackgroundColor(Color.parseColor("#CCCCCC"));
		titleLayout.setPadding(5, 5, 5, 0);
		addView(titleLayout);
		// ���Ʊ������
		drawTitleLayout();

		// ���listview
		listView = new ListView(getContext());
		listView.setPadding(5, 0, 5, 5);
		
		cAdapter = new CAdapter();
		listView.setAdapter(cAdapter);
		listView.setCacheColorHint(0);
		listView.setLayoutParams(FILL_FILL_LAYOUTPARAMS);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onItemClickListener != null)
					onItemClickListener.onItemClick(parent, view, position, id);
				setSelectedPosition(position);
				selectedPosition = position;
				cAdapter.notifyDataSetChanged();
			}
		});
		this.setBackgroundColor(Color.argb(100, 219, 219, 219));
		addView(listView);
	}

	/**
	 * �����иı�ʱ��ˢ����ʾ
	 */
	public void definedSetChanged() {
		calcColumnWidth();
		drawTitleLayout();
		cAdapter.notifyDataSetChanged();
	}

	/**
	 * ����ѡ��ʱ�ļ�����
	 * 
	 * @param onItemClickListener
	 */
	public void setOnItemClickListener(	AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	/**
	 * �����б�����ɫ, �����ɫ������Ϊ���ɫ
	 * 
	 * @param color
	 *            �б�����ɫ������Ϊ���
	 */
	public void setItemBackgroundColor(int... color) {
		rowsBackgroundColor = color;
	}

	/**
	 * ��������
	 */
	public int getCount() {
		if (data == null)
			return 0;
		return data.size();
	}

	/**
	 * ��ǰѡ������
	 * 
	 * @param position
	 * @return
	 */
	public String[] getItem(int position) {
		if (data == null)
			return null;
		return data.get(position);
	}

	/**
	 * ���õ�ǰѡ��λ��
	 * 
	 * @return
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	/**
	 * ��ǰѡ��λ��
	 * 
	 * @return
	 */
	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ���ñ�ѡ��ʱ�ı���ɫ
	 * 
	 * @param color
	 */
	public void setSelectedBackgroundColor(int color) {
		
		selectedBackgroundColor = color;
	}

	/**
	 * ���ñ��ⱳ��ɫ.
	 * 
	 * @param color
	 */
	public void setTitleBackgroundColor(int color) {
		titleBackgroundColor = color;
		titleLayout.setBackgroundColor(titleBackgroundColor);
	}

	/**
	 * ���ñ���������ɫ
	 * 
	 * @param color
	 */
	public void setTitleTextColor(int color) {
		titleTextColor = color;
		for (int i = 0; i < titleLayout.getChildCount(); i++) {
			((TextView) titleLayout.getChildAt(i)).setTextColor(titleTextColor);
		}
	}

	/**
	 * ��������������ɫ
	 * 
	 * @param color
	 */
	public void setContentTextColor(int color) {
		contentTextColor = color;
	}

	/**
	 * ���ñ��������С
	 * 
	 * @param szie
	 */
	public void setTitleTextSize(float szie) {
		titleTextSize = szie;
	}

	/**
	 * �������������С
	 * 
	 * @param szie
	 */
	public void setContentTextSize(float szie) {
		contentTextSize = szie;
	}

	/**
	 * 
	 * �趨�����Զ��п� ��0��ʼ����
	 * 
	 * @param index
	 */
	public void setAutoColumnWidth(int index) {
		autoWidthIndex = index;
		for (int i = 0; i < titleLayout.getChildCount(); i++) {
			TextView tv = ((TextView) titleLayout.getChildAt(i));
			if (i == autoWidthIndex)
				tv.setLayoutParams(FILL_FILL_LAYOUTPARAMS);
			else {
				tv.setLayoutParams(WAP_WAP_LAYOUTPARAMS);
				tv.setWidth(itemWidth[i]);
			}
		}
	}

	/**
	 * ���Ʊ���
	 */
	private void drawTitleLayout() {
		titleLayout.removeAllViews();
		for (int i = 0; i < title.length; i++) {
			TextView tv = new CTextView(titleLayout.getContext());
			tv.setTextColor(titleTextColor);
			tv.setGravity(Gravity.CENTER);
			tv.setText(title[i]);
			if (titleTextSize > 0) {
				tv.setTextSize(titleTextSize);
			}
			tv.setPadding(5, 0, 5, 0);
			if (i == autoWidthIndex)
				tv.setLayoutParams(TableView.FILL_FILL_LAYOUTPARAMS);
			else {
				tv.setWidth(itemWidth[i]);
			}
			titleLayout.addView(tv);
		}
	}

	/**
	 * �����п�
	 * 
	 * @return �Ƿ��иĶ�
	 */
	private boolean calcColumnWidth() {
		boolean result = false;

		float textSize = new TextView(getContext()).getTextSize();

		// ��������п�
		for (int i = 0; i < itemWidth.length; i++) {
			int w = (int) TableView.GetPixelByText(
					(titleTextSize > 0) ? titleTextSize : textSize, title[i]);
			if (itemWidth[i] < w) {
				itemWidth[i] = w;
				result = true;
			}
		}

		// ���������п�
		if (contentTextSize > 0) {
			textSize = contentTextSize;
		}
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < itemWidth.length && j < data.get(i).length; j++) {
				int w = (int) TableView.GetPixelByText(textSize, data.get(i)[j]);
				if (itemWidth[j] < w) {
					itemWidth[j] = w;
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * �����ַ�����ռ����
	 * 
	 * @param textSize
	 *            �����С
	 * @param text
	 *            �ַ���
	 * @return �ַ�����ռ����
	 */
	private static int GetPixelByText(float textSize, String text) {
		Paint mTextPaint = new Paint();
		mTextPaint.setTextSize(textSize); // ָ�������С
		mTextPaint.setFakeBoldText(false); // ����
		mTextPaint.setAntiAlias(true); // �Ǿ��Ч��

		return (int) (mTextPaint.measureText(text) + textSize);
	}

	/**
	 * ��Ҫ�õ�Adapter
	 * 
	 * @author Cdisk
	 * 
	 */
	class CAdapter extends BaseAdapter {

	

		public int getCount() {
			if (data == null)
				return 0;
			return data.size();
		}


		public Object getItem(int position) {
			if (data == null)
				return null;
			return data.get(position);
		}

	
		public long getItemId(int position) {
			return 0;
		}


		public View getView(int position, View convertView, ViewGroup parent) {

			// ��ʼ����layout
			LinearLayout contextLayout = new LinearLayout(
					TableView.this.getContext());

			String[] dataItem = data.get(position);

			if (getSelectedPosition() == position) { // Ϊ��ǰѡ����
				contextLayout.setBackgroundColor(selectedBackgroundColor);
			} else if (rowsBackgroundColor != null
					&& rowsBackgroundColor.length > 0) {
				contextLayout.setBackgroundColor(rowsBackgroundColor[position
						% rowsBackgroundColor.length]);
			}

			for (int i = 0; i < title.length; i++) {
				TextView tv = new CTextView(contextLayout.getContext());
				tv.setTextColor(contentTextColor);
				tv.setGravity(Gravity.LEFT);
				tv.setPadding(5, 5, 2, 5);
				if (i < dataItem.length) {
					tv.setText(dataItem[i]);
				}
				if (i == autoWidthIndex)
					tv.setLayoutParams(TableView.FILL_FILL_LAYOUTPARAMS);
				else {
					tv.setWidth(itemWidth[i]);
				}
				if (contentTextSize > 0) {
					tv.setTextSize(contentTextSize);
				}
				contextLayout.addView(tv);
			}

			return contextLayout;
		}

	}

	/**
	 * ��д��TextView
	 * 
	 * @author Cdisk
	 */
	class CTextView extends TextView {

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// Top
			canvas.drawLine(0, 0, this.getWidth() - 1, 0, WHITE_PAINT);
			// Left
			canvas.drawLine(0, 0, 0, this.getHeight() - 1, WHITE_PAINT);
			// Right
			canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
					this.getHeight() - 1, BLACK_PAINT);
			// Buttom
			canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
					this.getHeight() - 1, BLACK_PAINT);
		}

		public CTextView(Context context) {
			super(context);
		}
	}

}