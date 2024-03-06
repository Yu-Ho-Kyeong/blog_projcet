<template>
      <div class="container">
        <div class="row row-cols-1 g-3 con_wrap" v-if="state.boardItems !== null">
          <div class="col" v-for="(bitem, idx) in state.boardItems" :key="idx">
            <Board :bitem="bitem" v-if="bitem"/>
          </div>
        </div>
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
        boardItems: [],
        tagItems: []
      }, 
    };
  },
  mounted() {
    this.fetchBoardItems(); // 게시글 정보 조회
  },
  methods: {

    fetchBoardItems(boardListData) { 
      if(boardListData && boardListData.tagName){  //태그별 게시글 정보 조회 
        const tagName = boardListData.tagName;
        //console.log('tagName 있음');
        
        this.$axios.get(this.$boardUrl + "/all/getBoardsWithTag/" + tagName)
          .then((res) => {
            this.state.boardItems = res.data;
          })
          .catch((error) => {
            console.error('Board API 호출 중 에러 발생:', error);
          });
         
      }else{    //전체 게시글 정보 조회
        this.$axios.get(this.$boardUrl + "/all/getBoards")
        .then((res) => { 
          this.state.boardItems = res.data;
          console.log('res.data : ' + JSON.stringify(res.data));
        })
        .catch((error) => {
          console.error('Board API 호출 중 에러 발생:', error);
        });
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