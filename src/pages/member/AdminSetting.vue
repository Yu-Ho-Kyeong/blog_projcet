<template>
    <main>
        <!-- setting-info1-->
        <section class="member-info1">
            <div class="thumbnail-area">
                <img :src="this.input.imgPath" class="userImg" alt="profile" />
                <button class="btn thumbnail-upload" @click="onUploadReady">이미지 업로드</button>

                <!-- 업로드된 이미지가 있을 경우에만 노출 -->
                <div v-if="this.input.imgPath"> 
                    <button class="btn thumbnail-remove" @click="imageDelete1">이미지 제거
                    </button>
                </div>

                <!-- 이미지 업로드 버튼 클릭시 자동클릭 실행 -->
                <input class="file-input" v-if="this.uploadReady" ref="fileInput" type="file" 
                        accept="image/*" style="display:none" @change="onUpload"/>
            </div>

            <div class="info1-area">
                <div class="edtingInfo" v-if="edtingInfo">
                    <input type="text" class="edting-name" v-model="input.name" placeholder="이름을 입력해주세요"/>
                    <input type="text" class="edting-introduction" v-model="input.introduction" placeholder="소개글을 입력해주세요"/>
                    <button class="btn info-save" @click="saveInfo(1)">저장</button>
                </div>
                <div class="pre-edtingInfo" v-else>
                    <h2>{{input.name}}</h2>
                    <p>{{input.introduction}}</p>
                    <button class="btn info-modify" @click="modifyInfo(1)">수정</button>
                </div>  
            </div>
        </section>
        <!-- setting-info2 -->
        <section class="member-info2">
            <div class="info2-area-title">
                <div class="wrapper">
                    <div class="title-wrapper">
                        <h3>블로그 제목</h3>
                    </div>

                    <div class="edting" v-if="edtingInfo2">
                        <input type="text" class="edting-name" v-model="input.blogName" placeholder="블로그 제목"/>
                        <button class="btn info-save" @click="saveInfo(2)">저장</button>
                    </div>

                    <div class="block-for-mobile" v-else>
                        <div class="contents">{{input.blogName}}</div>
                        <div class="edit-wrapper">
                            <button class="btn title-modify" @click="modifyInfo(2)">수정</button>
                        </div>
                    </div>
                </div>

                <div class="description">개인 페이지의 좌측 상단에 나타나는 페이지 제목입니다.</div>
            </div>      
     
            <div class="info2-area-withdrawal">
                <div class="wrapper">
                    <div class="title-wrapper">
                        <h3>회원 탈퇴</h3>
                    </div>
                    
                    <div class="block-for-mobile">
                        <div class="contents">
                            <button color="red" class="btn btn-Withdrawal" @click="signout">회원 탈퇴</button>
                        </div>
                    </div>
                </div>
                
                <div class="description">탈퇴 시 작성하신 포스트 및 댓글이 모두 삭제되며 복구되지 않습니다.</div>
            </div>
        </section>
    </main>
</template>

<script>
export default ({
    data(){
        return{
            edtingInfo : false,
            edtingInfo2 : false,
            uploadReady: false,                 // 파일초기화를 위한 변수
            userNo : localStorage.getItem('user_no'),
            input:{
                image           : '',           // 이미지 객체
                name            : '',           // 사용자 이름 
                blogName        : '',           // 블로그 제목
                introduction    : '',           // 블로그 소개글 
                imgPath         : '',           // db에 저장될 이미지 경로
            } 
        }
    },
    mounted () {
        this.fnGetUserInfo();       // 회원정보 조회
    },
    methods:{
        onUploadReady(){
            this.uploadReady = true
            // Vue가 다음 DOM 업데이트 사이클에서 요소를 찾을 수 있도록 $nextTick 사용
            this.$nextTick(() => {
                // uploadReady가 true로 변경되었으므로 ref를 사용하여 fileInput에 접근하고 click() 메소드를 호출
                this.$refs.fileInput.click();
            });  
        },
        onUpload() {
            // input을 통해 들온 file 객체를 image 배열에 담는다.
            const file = this.$refs.fileInput.files[0];    

            // file 검사
            if(!file || !file.type.startsWith('image/')){
                alert('이미지 파일을 선택해주세요.');
                return
            }
            // 파일을 읽어오기 위해 FileReader 객체 생성
            const reader = new FileReader();
            // 콜백함수 정의
            reader.onload = (e) =>{
                // image 배열안에 읽어온 파일을 담는ㄴ다.
                this.input.image = e.target.result 
            }
            reader.readAsDataURL(file);
            
            this.uploadData = new FormData();
            this.uploadData.append('file', file);
            this.uploadData.append('userNo', this.userNo);
            //console.log('this.uploadData : '+ this.uploadData.getAll('file'));
            this.uploadFile();
        },

        uploadFile(){
            const apiUrl = '/api/admin/uploadImg';
            const config = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            };
            //console.log('this.uploadData2 : '+ this.uploadData.getAll('file'));
            this.$axios.post(apiUrl, this.uploadData, config)
            .then(()=>{
                //console.log('this.uploadData3 : '+ this.uploadData.getAll('file'));
                this.fnGetUserInfo();
                alert('업로드 완료');
            }).catch((err) => {
                if (err.message.indexOf('Network Error') > -1) {
                    alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
                }else if(err.response && err.response.status === 500){
                    alert('error');
                }
            });
        },
        // 회원정보 불러오기
        fnGetUserInfo(){
            const apiUrl = '/api/all/getAdminSetInfo/' + this.userNo
            this.$axios.get(apiUrl)
            .then((res)=>{
                this.input.name = res.data.name;
                this.input.introduction = res.data.introduction;
                this.input.blogName = res.data.blogName;
                this.input.imgPath = res.data.imgPath;

                this.$store.commit('setAccount', {
                    name: this.input.name,
                    introduction: this.input.introduction,
                    blogName: this.input.blogName,
                    imgPath: this.input.imgPath,
                });
                //console.log('res.data : ' + JSON.stringify(res.data));
            }).catch((err) => {
                if (err.message.indexOf('Network Error') > -1) {
                    alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
                }
            });
        },

        // 이미지 제거
        // imageDelete() {
            
        // },

        // clearImage() {
        //     // 이미지 업로드 기능을 비활성화(false)
        //     this.uploadReady = false
        //     this.$nextTick(() => {
        //         this.uploadReady = true
        //     })
        // },

        // 이름, 소개글 수정 div 태그 변환
        modifyInfo(type) {
            if(type === 1){
                this.edtingInfo = true
            }else{
                this.edtingInfo2 = true
            }   
        },
        saveInfo(type){     // 회원정보 수정
            let apiUrl='';
            let request= {};
            if(type ===1){
                apiUrl = '/api/admin/adminUpdateInfo';
                request = {
                    "userNo"        : this.userNo,
                    "name"          : this.input.name,
                    "introduction"  : this.input.introduction
                }
            }else{
                apiUrl = '/api/admin/adminUpdateBlogName';
                request = {
                    "userNo"        : this.userNo,
                    "blogName"      : this.input.blogName,
                }
            }
            
            this.$axios.post(apiUrl,request)
            .then(()=>{
                this.fnGetUserInfo();
                this.edtingInfo = false;
                this.edtingInfo2 = false;
            }).catch((err)=>{
                if (err.message.indexOf('Network Error') > -1) {
                    alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
                }
            })
        },
        // 회원 탈퇴
        signout() { // 탈퇴전 비밀번혼 체크 페이지로 이동
            this.$router.push({
                name: 'SignOut'
            })
        },
    }
})
</script>


<style scoped>

    main {
        max-width: 800px;
        margin: 0 auto;
        padding: 20px;
    }

    .member-info1 {
        margin-top: 5em;
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 20px;
    }

    .thumbnail-area {
        flex-direction: column;
        display: inline-flex;
    }

    .thumbnail-area img {
        width: 100px;
        height: 100px;
        border-radius: 50%;
        object-fit: cover;
        margin-bottom: 10px;
    }

    .btn.thumbnail-upload,
    .btn.thumbnail-remove {
    margin-right: 10px;
    padding: 5px 10px;
    font-size: 12px;
    border-radius: 5px;
    }

    .info1-area {
    flex-grow: 1;
    margin-left: 20px;
    }

    .info1-area h2 {
    margin-bottom: 10px;
    }
    .edtingInfo, .pre-edtingInfo{
        flex-direction: column;
    }
    .pre-edtingInfo h2{
        font-size: 2.5em;
        font-weight: bold;
    }
    .pre-edtingInfo p{
        height: 150px;
    }
    .info-modify {
    padding: 8px 16px;
    font-size: 14px;
    border-radius: 5px;
    background-color: teal;
    color: white;
    cursor: pointer;
    }

    .member-info2 {
    border-top: 1px solid #ccc;
    padding-top: 20px;
    }

    .info2-area-title,
    .info2-area-email,
    .info2-area-withdrawal {
    margin-bottom: 20px;
    border-bottom: 1px solid #ccc;
    }

    .title-wrapper {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 0.5em;
    }
    
    .block-for-mobile {
        display: flex;
        align-items: center;
    }

    .title-wrapper h3 {
    margin-bottom: 10px;
    }
    .description{
        margin-bottom: 1rem;
    }
    .contents {
    font-weight: bold;
    margin-bottom: 10px;
    }

    .edit-wrapper button
    {
        padding: 8px 16px;
        font-size: 14px;
        border-radius: 5px;
        cursor: pointer;
        margin-left: 1.5rem;
    }

    .edit-wrapper button {
    background-color: transparent;
    color: teal;
    border: 1px solid teal;
    margin-right: 10px;
    }

    .btn-Withdrawal {
    background-color: red;
    color: white;
    border: none;
    }
</style>


