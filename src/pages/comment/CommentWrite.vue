<template>
    <div>
        <textarea placeholder="댓글을 작성하세요" class="comment-write-input"
                  style="height: 70px;" v-model="commentContent">
        </textarea>
        <div class="buttons-wrapper">
          <button class="btn btn-comment-write" @click="writeComment">댓글 작성</button>
        </div>
    </div>
</template>

<script>

export default ({
    props:{
        parentBoardNo       : Object,
        parentUserNo        : Object,
        parentCommentNo     : Object
    },
    data (){
        return{
            commentUserId: localStorage.getItem('user_id'),
            commentContent: '' 
        }
    },
    methods:{
        writeComment() {  // 댓글 작성 
            if (!this.commentContent.trim()) {
                alert('댓글 내용을 입력해주세요.');
                return;
            }

            if(!this.$store.state.account.isLogin)      // 로그인시 isLogin = true
            {
                alert('댓글을 작성하시려면 로그인해주세요.');
                this.$router.push({
                    name: 'Login'
                })
            }
            
            const apiUrl = this.$commentUrl + '/user/addComment/' + this.parentBoardNo
            const form = {
                "boardNo"          : this.parentBoardNo,
                "userNo"           : this.parentUserNo,
                "commentContent"   : this.commentContent,
                "parentCommentNo"  : this.parentCommentNo
            };
            
            console.log('전송 데이터:', form);
            this.$axios.post(apiUrl, form)
            .then(() => {
                alert('댓글이 저장되었습니다.')
                // form을 부모컴포넌트로 전송하여 부모컴포넌트에서 조작
                this.$emit('comment-submitted', form);
    
                }).catch((err) => {
                    if (err.message.indexOf('Network Error') > -1) {
                        alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
                    }else if(err.response && err.response.status === 500){
                        alert('error');
                    }
                })

                this.commentContent = '';       // 댓글내용 초기화
            },
    }
})
</script>

<style scoped>
    .comment-write-input, 
    .re-comment-input {
        resize: none;
        width: 100%;
        box-sizing: border-box;
        margin-bottom: 10px;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
    .buttons-wrapper {
        display: flex;
        justify-content: flex-end;
    }
    .btn-comment-write{
        padding: 8px 12px;
        border: 1px solid #007BFF;
        border-radius: 4px;
        background-color: #007BFF;
        color: #fff;
        cursor: pointer;
        transition: background-color 0.3s;
    }
</style>
