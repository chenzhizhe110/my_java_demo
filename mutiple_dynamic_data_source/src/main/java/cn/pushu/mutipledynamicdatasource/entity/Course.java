package cn.pushu.mutipledynamicdatasource.entity;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> Course<br>
 * <b>projectName:</b> myTest<br>
 * <b>description:</b> TODO <br>
 * <b>date:</b> 2024/4/1210:40
 */
public class Course {
    private  long cid;
    private String cname;
    private String  userId;
    private String cstatus;
    private String date;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCstatus() {
        return cstatus;
    }

    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Course{");
        sb.append("cid=").append(cid);
        sb.append(", cname='").append(cname).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", cstatus='").append(cstatus).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
