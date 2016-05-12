package com.xyc.wyatt.domain;

import cn.bmob.v3.BmobObject;

import com.xyc.wyatt.annotation.Column;
import com.xyc.wyatt.annotation.ID;
import com.xyc.wyatt.annotation.TableName;

@TableName("wt_comments")
public class Comments extends BmobObject{
	/*
	 * 评论ID 类型 上级评论ID 运动记录ID 评论内容 评论时间 id commentType rid content ctime save1
	 * save2
	 */
	@Column("id")
	@ID(autoincrement = true)
	private int id;
	@Column("commentType")
	private String commentType;
	@Column("rid")
	private int rid;
	@Column("uid")
	private int uid;
	@Column("content")
	private String content;
	@Column("ctime")
	private String ctime;
	@Column("cname")
	private String cname;
	@Column("dobjectId")
	private String dobjectId;
	@Column("userName")
	private String userName;
	@Column("avatarPath")
	private String avatarPath;
	
	private int did;
	
	
	//@Column("save2")
	private String save2;
	@Column("save1")
	private String save1;
	
	

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getSave1() {
		return cname;
	}

	public void setSave1(String save1) {
		this.cname = save1;
	}

	public String getSave2() {
		return save2;
	}

	public void setSave2(String save2) {
		this.save2 = save2;
	}

	public String getDobjectId() {
		return dobjectId;
	}

	public void setDobjectId(String dobjectId) {
		this.dobjectId = dobjectId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

}
