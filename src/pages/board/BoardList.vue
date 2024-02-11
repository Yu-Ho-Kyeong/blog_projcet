<template>
    <div class="col">
          <div class="card shadow-sm" v-on:click="fnView(`${bitem.boardNo}`)">
            <img :src="bitem.imgPath" alt="썸네일">
            <div class="card-body">
              <p class="card-text">{{bitem.boardTitle}}</p>
              <p class="card-text" v-html="bitem.boardContent"></p>
              <div class="d-flex justify-content-between align-items-center">
                <!-- <div class="btn-group">
                  <button type="button" class="btn btn-sm btn-outline-secondary">보기</button>
                  <button type="button" class="btn btn-sm btn-outline-secondary">수정</button>
                </div>   -->
                <!-- <small class="text-body-secondary">{{currentDate}}</small>
                <small class="text-body-secondary">{{formatRegDate(item.regDate)}}</small> -->
                <span class="new badge bg-danger" v-if="formatRegDate(bitem.regDate) == currentDate">new</span>
                <small class="text-body-secondary" v-else>{{formatRelativeDate(bitem.regDate)}}</small>
              </div>
            </div>
          </div>
        </div>
</template>

<script>
import { formatDistanceToNow } from 'date-fns';
import koLocale from 'date-fns/locale/ko';
	export default {
		name :'Board',
    props:{
      bitem: Object // 객체 데이터타입 지정
    }, 
    data() {
      return {
        currentDate: '',
        requestBody: {}, //리스트 페이지 데이터전송
        list: {}, //리스트 데이터
        no: '', //게시판 숫자처리
        paging: {
          block: 0,
          end_page: 0,
          next_block: 0,
          page: 0,
          page_size: 0,
          prev_block: 0,
          start_index: 0,
          start_page: 0,
          total_block_cnt: 0,
          total_list_cnt: 0,
          total_page_cnt: 0,
        }, //페이징 데이터
        page: this.$route.query.page ? this.$route.query.page : 1,
        size: this.$route.query.size ? this.$route.query.size : 10,
        keyword: this.$route.query.keyword,
        paginavigation: function () { //페이징 처리 for문 커스텀
          let pageNumber = [] //;
          let start_page = this.paging.start_page;
          let end_page = this.paging.end_page;
          for (let i = start_page; i <= end_page; i++) pageNumber.push(i);
          return pageNumber;
        }
      }
      
    },
    mounted() {
    // 컴포넌트가 마운트된 후에 현재 날짜를 설정
    this.setCurrentDate();
    this.fnGetList();
    },
    methods: {
      formatRegDate(regDate) {
        const formattedDate = new Date(regDate).toISOString().split('T')[0];
      return formattedDate;
      },
      formatRelativeDate(regDate) {
      return formatDistanceToNow(new Date(regDate), { addSuffix: true, locale: koLocale });
      },
      setCurrentDate() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0');
      const day = String(today.getDate()).padStart(2, '0');

      // 날짜를 YYYY-MM-DD 형식으로 설정
      this.currentDate = `${year}-${month}-${day}`;
      },
      fnView(boardNo) {
        this.requestBody.boardNo = boardNo
        this.$router.push({
          path: '/board/detail',
          query: this.requestBody
        })
      },
      fnGetList() {
        this.requestBody = { // 데이터 전송
          keyword: this.keyword,
          page: this.page,
          size: this.size
        }
      }
    }
  }
</script>

<style scoped>
</style>

