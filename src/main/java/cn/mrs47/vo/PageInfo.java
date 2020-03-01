package cn.mrs47.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author mrs47
 */
public class PageInfo<T> implements Serializable {

    /**
     * pageNumber:   当前页码
     * total:        总条数
     * every:        每隔 “every”
     * indexPage:    起始页 计算方法见 setIndexPage()
     * nextPage:     当前页的下一页
     * previousPage: 当前页的下一页
     * totalPage:    总页数
     * endPage:      最后一页  计算方法见 setIndexPage()
     * <T> data:     页面数据内容
     */
    private Integer pageNumber;
    private Integer total;
    private Integer every;
    private Integer indexPage;
    private Integer nextPage;
    private Integer previousPage;
    private Integer totalPage;
    private Integer endPage;
    private List<T> data;

    public PageInfo(Integer count,Integer every,Integer page,List<T> data){
        this.every=every;
        this.total=count;
        this.totalPage=(count%every==0)?(count/every):(count/every+1);
        if (page>this.totalPage ){
            page=this.totalPage;
        }
        this.pageNumber = page;
        this.data=data;
        setNextPage();
        setPreviousPage();
        setIndexPage();
    }

    public Integer getEndPage() {
        return endPage;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer page) {
        if (page>this.totalPage ){
            page=this.totalPage;
        }
        this.pageNumber = page;
        setNextPage();
        setPreviousPage();
        setIndexPage();
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getEvery() {
        return every;
    }

    public void setEvery(Integer every) {
        if (every<1){
            every=1;
        }
        this.every = every;
    }

    public Integer getIndexPage() {
        return indexPage;
    }

    /**
     * 初始10格
     */
    public void setIndexPage() {
        Integer indexPage=this.pageNumber;
        if ((indexPage-5)>=1){
            if (totalPage>10){
                if (indexPage<totalPage-4){
                    indexPage-=4;
                }else {
                    indexPage=totalPage-9;
                }
            }
        }else {
            indexPage=1;
        }

        if (totalPage > 10){
            this.endPage=indexPage+9;
        }else {
            this.endPage=totalPage;
        }
        this.indexPage = indexPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage() {
        this.nextPage=this.pageNumber+1;
        if (this.nextPage>this.totalPage){
            this.nextPage=this.totalPage;
        }
    }

    public Integer getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage() {
        this.previousPage=this.pageNumber-1;
        if (this.previousPage<1){
            this.previousPage=1;
        }
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer count) {
        this.totalPage=(count%every==0)?(count/every):(count/every+1);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
