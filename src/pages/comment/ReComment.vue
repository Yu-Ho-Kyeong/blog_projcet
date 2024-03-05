<template>
    <div class="recomment_form">
        <!-- 대댓글 내용 및 폼 -->
        <div class="comment-button">
            <a @click="showRecomment">{{recommmentCount}}개의 답글</a>
            <!-- <a v-else @click="showSave">답글 달기</a> -->
        </div>
            
        <!-- 등록된 답글 -->
        <div v-if="(showReplyForm) && cItem && cItem.children" class="registered-recomment-wrapper">
            <div class="registered-recomment" v-for="(reComment, index) in cItem.children" :key="index">
                <div class="comment-profile">
                    <img :src="reComment.user.imgPath" alt="comment-user-profile"/>
                    <div class="comment-info">
                    <div class="userId">
                        {{reComment.user.name}}
                    </div>
                    <div class="regDate">{{formatRegDate(reComment.regDate)}}</div>
                    </div>
                </div>
                <div class="common-buttons">
                    <button type="button" @click="changeDiv(2, index, reComment)">답글</button>
                    <button type="button" v-if="reComment.user.userId === this.$store.state.account.userId"
                            v-on:click="changeDiv(1, index)">수정</button>
                    <button type="button" v-if="reComment.user.userId === this.$store.state.account.userId" 
                            v-on:click="fnDelete(reComment)">삭제</button>
                </div>


                <div class="btn-output-section">
                    <!-- 댓글 내용 -->
                    <div class="comment-content" v-if="updatedReComments[index].divType == 'comment-content'">
                        <p>{{reComment.commentContent}}</p>
                    </div>

                    <div class="comment-modify" v-else-if="updatedReComments[index].divType == 'comment-modify'">
                        <button class="btn_comment_cancle" @click="changeDiv(3, index)">취소</button>
                        <button class="btn_comment_modify" @click="fnUpdate(reComment)">수정하기</button>
                        <textarea v-model="reComment.commentContent" class="comment-modify-input"></textarea>
                    </div>

                    <div class="recomment-register" v-else>
                        <p>{{reComment.commentContent}}</p>
                        <CommentWrite :parentBoardNo ="this.parentBoardNo" :parentUserNo="this.parentUserNo"
                                      :parentCommentNo ="this.commentNo"
                                      @comment-submitted="handleCommentSubmitted"/>
                    </div>      
                </div>

                <ReComment v-if="`reComment.children && reComment.children.length > 0`"
                            :cItem="reComment" 
                            :parentBoardNo ="this.parentBoardNo" 
                            :parentUserNo="this.parentUserNo"
                            @comment-submitted="handleCommentSubmitted" />
            </div>
        </div> 
    </div>
    
</template>

<script>
import CommentWrite from '@/pages/comment/CommentWrite.vue';
export default({
    components: { CommentWrite},
    props:{
        cItem: {
            type : Object,
            required: true,
        },
        parentBoardNo: {
            type : Object,
            required: true,
        },
        parentUserNo: {
            type : Object,
            required: true,
        }
    },

    data(){
        return{
            state:{
                commentUpdateItem : [],      // 현재 게시글 정보
            },
            recommmentCount     : 0,
            showReplyForm       : false,
            showSaveBool        : false,
            updatedReComments   : [],
            reCommentUserId: localStorage.getItem('user_id'),
        }
    },

    mounted(){
        //console.log('test1 : ' + this.commentNo);
        this.fnGetCnt();
        this.cItem.children.forEach(reComment =>{
            if(reComment.id.length !==0){
                //const paretnId = reComment.id
                //const boardNo = reComment.baordNo
            }   
            //console.log('reComment : ' + JSON.stringify(reComment));
                
        })

        this.updatedReComments = this.cItem.children.map(comment => { 
            return {
                ...comment,
                divType: 'comment-content'
            };
            
        });
    },

    methods:{
        // 답글 숨기기 로직
        showRecomment() {
            if(this.recommmentCount !==0){
                this.showReplyForm = !this.showReplyForm;
            }
        },
        showSave(){
            this.showSaveBool = !this.showSaveBool;
            this.showReplyForm = !this.showReplyForm;
        },
        cancelReply() {
            this.showReplyForm = false;
        },

        fnUpdate(reComment){
            this.commentUpdateItem = [];
            this.commentUpdateItem.commentNo = reComment.id;
            this.commentUpdateItem.commentContent = reComment.commentContent;

            const apiUrl = this.$commentUrl + '/user/modifyComment/' + this.commentUpdateItem.commentNo;
            console.log('content : ' + this.commentUpdateItem.commentContent);
            this.$axios.put(apiUrl, {"commentContent"   : this.commentUpdateItem.commentContent}, {
                Headers : {
                    'Content-Type' : 'application/json',
                }
            })
            .then(() => {
                //this.$emit('comment-submitted', this.commentUpdateItem.commentContent);
                const index = this.cItem.children.indexOf(reComment);
                this.updatedReComments[index].divType ='comment-content';
            }).catch((err) => {
                if (err.message.indexOf('Network Error') > -1) {
                    alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
                }   
            })
        },
        formatRegDate(regDate) {
            const formattedDate = new Date(regDate).toISOString().split('T')[0];
            return formattedDate;
        },

        changeDiv(type, index, reComment) {
            let newDivType = '';
            if(type === 1 ){
                newDivType = 'comment-modify';
            }else if(type === 2 ){
                if(this.updatedReComments[index].divType === 'comment-write'){
                    newDivType = 'comment-content';
                }else{
                    newDivType = 'comment-write';
                }
                if(reComment){
                    //console.log('cItem : ' + JSON.stringify(cItem));
                    this.commentNo = reComment.id;
                    console.log('클릭한 부모댓글의 commentNo : ' + this.commentNo);
                }      
            }else{
                newDivType = 'comment-content';
            }
            //const newDivType = type === 1 ? 'comment-modify' : 'comment-content';
            this.updatedReComments[index].divType = newDivType;
            console.log('this.updatedReComments[index].divType : ' + this.updatedReComments[index].divType);
        },
        
        fnGetCnt(){
            if(this.cItem.children){
                this.cItem.children.forEach( child =>{
                    const parentCommentNo = child.parent.id;
                    const boardNo = child.boardNo;

                    this.$axios.get(this.$commentUrl + '/all/getReCount/' + boardNo + "/" + parentCommentNo)
                    .then((res) =>{
                        this.recommmentCount = res.data;
                        console.log('fnGetCnt() : ' + this.recommmentCount);
                    })
                    .catch((err) => {
                        if (err.message.indexOf('Network Error') > -1) {
                            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
                        }
                    })  
                })
            }         
        },
        fnDelete(reComment){
            if(confirm('정말 삭제하시겠습니까?')){
                const commentNo = reComment.id;
                //console.log('삭제할 commentNo: ', commentNo);
                this.$axios.delete(this.$commentUrl + "/user/deleteComment/" + commentNo)
                .then(() => {
                    alert('삭제되었습니다.')
                    this.$emit('comment-submitted', reComment); 
                }).catch((err) => {
                    console.log(err);
                })
            }else{
                return false;
            }  
        },
        handleCommentSubmitted(newCommentData){
            console.log('new comment : '+ JSON.stringify(newCommentData));
            this.$emit('comment-submitted', newCommentData);
        },
    }
    
})
</script>

<style scoped>
    .comment-container {
        margin-top: 20px;
    }
    .registered-recomment-wrapper{
        margin-top: 3.5em;
        /* margin-left: 50px; */
        width:100%;
    }

    .registered-recomment{
        width: 90%;
        background-color: #f7f7f7;
        margin-bottom: 2.5em;
        /* justify-content: flex-end; */
        align-items: center;
    }

    .comment-wrapper {
        padding: 10px;
    }

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

    .comment-modify-input {
        resize: none;
        width: 100%;
        box-sizing: border-box;
        margin-bottom: 10px;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
        }

    .recomment-register{
        width: 100%;
    }
    .btn-comment-write,
    .btn-recomment-register,
    .btn-recomment-cancle {
        padding: 8px 12px;
        border: 1px solid #007BFF;
        border-radius: 4px;
        background-color: #007BFF;
        color: #fff;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .btn-comment-write:hover,
    .btn-recomment-register:hover,
    .btn-recomment-cancle:hover {
        background-color: #0056b3;
    }

    .common-buttons {
        margin-top: 10px;
        display: flex;
        justify-content: flex-end;
        align-items: center;
    }

    .common-buttons button {
        color: #495057;
        margin-right: 10px;
        border : none;
        background-color: #fff;
    }

    .registered-comment-wrapper {
    margin-top: 15px;
    }
    .comment-modify{
        width: 100%;
    }

    .textera {
        width: 100%;
        padding: 10px;
        resize: none; 
    }

    .comment-profile {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
    }

    .comment-profile img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        object-fit: cover;
        margin-right: 1.5em;
    }

    .comment-info {
    flex: 1;
    }

    .comment-info .userId {
    font-weight: bold;
    }

    .regDate {
    color: #777;
    }

    .comment-content {
        margin-top: 2.5rem;
        margin-left: 1em;
        margin-bottom: 2.5rem;
    }

    .recomment_form {
        width:100%;
        width: 100%;
    }

    .comment-button a {
        color: #007BFF;
        cursor: pointer;
        text-decoration: underline;
    }

    .comment-button a:hover {
        color: #0056b3;
    }

    .re-comment-input {
        width: calc(100% - 16px); /* 부모 padding 고려하여 조정 */
    }

    .btn_comment_modify, .btn_comment_cancle { 
    /* padding: 10px 20px; */
    margin: 0.5em;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    /* transition: background-color 0.3s; */
    }
</style>