package com.zyf.device;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;

import com.zyf.model.MyData;

import java.util.List;

//自定义的ExpandListAdapter
public class BaseExpandableAdapter extends BaseExpandableListAdapter {
	protected BaseActivity a;
	private MyData groups;
	private List<MyData> childs;
	private int layout_group, layout_child;

	/**
	 * 构造函数: 参数1:context对象 参数2:一级列表数据源 参数3:二级列表数据源
	 */
	public BaseExpandableAdapter(BaseActivity a, MyData groups,
                                 List<MyData> childs, int layout_group, int layout_child) {
		this.a = a;
		this.groups = groups;
		this.childs = childs;
		this.layout_group = layout_group;
		this.layout_child = layout_child;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
		LinearLayout v = (LinearLayout) a.getLayoutInflater().inflate(
				layout_child, null);
		displayChild(v, groupPosition, childPosition);
		return v;
	}

	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 获取一级列表View对象
	public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
		// 获取一级列表布局文件,设置相应元素属性
		LinearLayout v = (LinearLayout) a.getLayoutInflater().inflate(
				layout_group, null);
		displayGroup(v, groupPosition);
		return v;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 虚方法，等待子类覆盖
	 *
	 * @param v
	 * @param groupPosition
	 */
	protected void displayGroup(View v, int groupPosition) {
	}

	/**
	 * 虚方法，等待子类覆盖
	 * 
	 * @param v
	 * @param groupPosition
	 * @param childPosition
	 */
	protected void displayChild(View v, int groupPosition, int childPosition) {
	}

}
