<template>
  <div class="container">
    <div class="row row-cols-1 g-3 con_wrap" v-if="`state.boardItems.length > 0`">
      <div class="col" v-for="(bitem, idx) in visibleBoardItems" :key="idx">
        <Board :bitem="bitem" v-if="bitem"/>
      </div>
    </div>
    <button v-if="showLoadMoreButton" @click="loadMore" class="btn btn-primary mt-3">더 보기</button>
  </div>
</template>

<script>
import Board from '@/pages/board/BoardList.vue';

export default {
  components: { Board },
  name: 'Home',
  props: ['boardListData'],
  data() {
    return {
      state: {
        boardItems      : [],     //전체 게시글 조회시 데이터가 저장되는 배열
        boardByTagItems : []      //태그명으로 게시글 조회시 데이터가 저장되는 배열
      },
      paging: {
        page: 0,                  // 페이지 초기값
        pageSize: 10,             // 한 페이지에 보여지는 게시글수
        totalItems: 0             // 전체개수
      },
      visibleBoardItems: [],      // page에 따른 boardList에 보여지는 list 저장
      showLoadMoreButton: false   // 더보기 버튼 여부
    };
  },
  mounted() {
    this.fetchBoardItems(); // 게시글 정보 조회
  },
  methods: {
    fetchBoardItems(boardListData) {  // 게시글 정보 조회
      const params = {
        page: this.paging.page,
        size: this.paging.pageSize
      };

      console.log('params : ' + JSON.stringify(params));

      if(boardListData && boardListData.tagName){  //태그별 게시글 정보 조회 
        const tagName = boardListData.tagName;
        console.log('tagName : ' +tagName);
        this.$axios.get(this.$boardUrl + "/all/getBoardsWithTag/" + tagName, { params })
          .then((res) => {
            this.state.boardByTagItems = [...this.state.boardByTagItems, ...res.data.content];
            //this.state.boardByTagItems = res.data.content;
            this.paging.totalItems = res.data.totalElements;
            this.updateVisibleBoardItems();
          })
          .catch((error) => {
            console.error('Board API 호출 중 에러 발생:', error);
          });
      }else{    //전체 게시글 정보 조회
        this.$axios.get(this.$boardUrl + "/all/getBoards", { params })
        .then((res) => { 
          this.state.boardItems = [...this.state.boardItems, ...res.data.content];
          console.log('this.state.boardItems' + this.state.boardItems);
          //this.state.boardItems = res.data.content;
          this.paging.totalItems = res.data.totalElements;
          this.updateVisibleBoardItems();
        })
        .catch((error) => {
          console.error('Board API 호출 중 에러 발생:', error);
        });
      }
    },
    updateVisibleBoardItems() {
      const start = 0;    // 무한스크롤 start는 고정으로 0
      const end = (this.paging.page +1) * 10; // page가 증가함에따라 10/20/30/40 이런식으로 증가
      const total = this.paging.totalItems;

      if (this.boardListData && this.boardListData.tagName) { // 태그명으로 조회시
        this.visibleBoardItems = this.state.boardByTagItems.slice(start, end);
      } else {  // 전체 게시글 조회시
        this.visibleBoardItems = this.state.boardItems.slice(start, end);
      }

      if (total > end) {
        // 더보기 버튼 여부
        this.showLoadMoreButton = true;
      }
    },
    // 더보기 버튼
    loadMore() {
      this.paging.page++;
      if((this.paging.page *10) > this.paging.totalItems ){
        alert('더이상 게시글이 없습니다.');
        return false;
      }
      if (this.boardListData && this.boardListData.tagName) { //태그명으로 게시글 조회시
        this.fetchBoardItems(this.boardListData);
      }else{    // 전체 게시글 조회시
        this.fetchBoardItems();
      }
    }
  }
}
</script>
<style scoped>

  .container{ 
    max-width: 1080px; 
    position: relative;
  }
  
  .con_wrap{
    /* padding-left: 220px; */
    /* float: right;
    width: 80%; */
  }
</style>