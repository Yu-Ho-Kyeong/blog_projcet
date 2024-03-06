<template>
    <div class="col">
          <div class="card shadow-sm" v-on:click="fnView(`${bitem.board.boardNo}`)">
            <div v-if="bitem.imgs !==null">
              <div v-for="(img, index) in bitem.imgs" :key="img.boardImgNo">
                <img v-if="index === 0" :src="img.imgPath" alt="썸네일" class="board-list-img">
              </div>
            </div>
            
            <div class="card-body">
              <p class="card-text boardTitle">{{bitem.board.boardTitle}}</p>
              <p class="card-text boardContent">{{ truncateContent(getPlainText(bitem.board.boardContent),150) }}</p>
              <div v-if="bitem.tags !==null">
                <div v-for="tag in bitem.tags" :key="tag.tagNo">
                  <p class="card-text tag">{{ tag.tagName }}</p>
                </div>
              </div>
              <div v-else>
                <p class="card-text tag">{{ bitem.tagName }}</p>
              </div>
              
              <div class="d-flex justify-content-between align-items-center">
                <span class="new badge bg-danger" v-if="formatRegDate(bitem.board.regDate) == currentDate">new</span>
                <small class="text-body-secondary" v-else>{{formatRelativeDate(bitem.board.regDate)}}</small>
                <p class="card-text commentCnt"> {{commentCnt}}개의 댓글</p>
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
      bitem: Object 
    }, 
    data() {
      return {
        currentDate: '',
        commentCnt : 0,
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
      this.setCurrentDate();    // 현재 날짜 가져오기
      this.fnGetList();         
      this.fnGetCnt();          // 게시글당 댓글 count
    },
    methods: {
      getPlainText(html) {
        const div = document.createElement('div');
        div.innerHTML = html;
        return div.innerText.trim();
      },
      formatRegDate(regDate) {
        if (!regDate) return ''; // 유효하지 않은 날짜 값인 경우 빈 문자열 반환

        const dateObject = new Date(regDate);
        if (isNaN(dateObject.getTime())) return ''; // 유효하지 않은 날짜 값인 경우 빈 문자열 반환

        const formattedDate = dateObject.toISOString().split('T')[0];
        return formattedDate;
      },
      formatRelativeDate(regDate) {
        if (!regDate) return ''; // 유효하지 않은 날짜 값인 경우 빈 문자열 반환

        const dateObject = new Date(regDate);
        if (isNaN(dateObject.getTime())) return ''; // 유효하지 않은 날짜 값인 경우 빈 문자열 반환

        return formatDistanceToNow(dateObject, { addSuffix: true, locale: koLocale });
      },
      // 현재 날짜 세팅
      setCurrentDate() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');

        // 날짜를 YYYY-MM-DD 형식으로 설정
        this.currentDate = `${year}-${month}-${day}`;
      },
      // 게시물 정보 조회
      fnView(boardNo) {
        //const { page, size } = this;
        this.requestBody.boardNo = boardNo
        console.log('this.requestBody : ' + JSON.stringify(this.requestBody));
        this.$router.push({
          name: 'BoardDetail',
          query: this.requestBody
        })
      },
      
      fnGetList() {
        this.requestBody = { // 데이터 전송
          keyword: this.keyword,
          page: this.page,
          size: this.size
        }
      },
      // 게시물 내용 글자수 표시 제한
      truncateContent(content, maxLength) {
        if (!content) return '';
        if (content.length <= maxLength) return content;
        return content.slice(0, maxLength) + '...';
      },
      fnGetCnt(){   // 게시글당 댓글 count
        const boardNo = this.bitem.board.boardNo
        const apiUrl = this.$commentUrl + '/all/getCount' + '/' +  boardNo
        this.$axios.get(apiUrl)
          .then((res)=>{
            //console.log('res : '+ res.data);
            this.commentCnt = res.data
          }).catch((err)=>{
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
            }
          })
      }
    }
  }
</script>

<style scoped>
  .tag{
    background-color: aquamarine;
    width: 3em;
    border-radius: 7px;
  }
  .boardTitle{
    font-weight: bold;
    font-size: 1.5em;
  }
  .board-list-img{
    width: 100%;
    height: 500px;
  }
  .card-body{
    border: none !important;
  }
</style>

