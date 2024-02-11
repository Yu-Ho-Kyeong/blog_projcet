<template>
    <main>
        <!-- setting-info1-->
        <section class="member-info1">
            <div class="thumbnail-area">
                <img :src="input.image" alt="profile" />
                <button class="btn thumbnail-upload" @click="onUploadReady">이미지 업로드</button>

                <!-- 업로드된 이미지가 있을 경우에만 노출 -->
                <div v-if="this.input.imagePath"> 
                    <button class="btn thumbnail-remove" @click="imageDelete1">이미지 제거
                    </button>
                </div>

                <!-- 이미지 업로드 버튼 클릭시 자동클릭 실행 -->
                <input class="file-input" v-if="this.input.uploadReady" ref="fileInput" type="file" 
                        accept="image/*" style="display:none" @change="onUpload"/>
            </div>

            <div class="info1-area">
                <div class="edting" v-if="edting">
                    <input type="text" class="name" v-model="name" placeholder="이름을 입력해주세요"/>
                    <input type="text" class="introduction" v-model="introduction" placeholder="소개글을 입력해주세요"/>
                    <button class="btn info-save" @click="saveInfo">저장</button>
                </div>
                <div class="pre-edting" v-else>
                    <h2>이름</h2>
                    <p>블로그 소개글</p>
                    <button class="btn info-modify" @click="modifyInfo">수정</button>
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
                    <div class="block-for-mobile">
                        <div class="contents">블로그 제목</div>
                        <div class="edit-wrapper">
                            <button class="btn title-modify">수정</button>
                        </div>
                    </div>
                </div>

                <div class="description">개인 페이지의 좌측 상단에 나타나는 페이지 제목입니다.</div>
            </div>

            <!-- <div class="info2-area-title">
                <div class="wrapper">
                    <div class="title-wrapper">
                        <h3>소셜 정보</h3>
                    </div>
                    
                    <div class="block-for-mobile">
                        <div class="contents">
                            <ul class="sc-iLOkMM hfZbzs"></ul>
                            <button class="sc-hAWBJg ccYtRc">정보 추가</button>
                        </div>
                    </div>
                </div>
                
                <div class="description">포스트 및 블로그에서 보여지는 프로필에 공개되는 소셜 정보입니다.</div>
            </div> -->
            
            <div class="info2-area-email">
                <div class="wrapper">
                    <div class="title-wrapper">
                        <h3>이메일 주소</h3>
                    </div>
                    
                    <div class="block-for-mobile">
                        <div class="contents">kos12753@gmail.com</div>
                        <div class="edit-wrapper">
                            <button class="btn email-mdify">변경</button>
                        </div>
                    </div>
                </div>
                
                <div class="description">회원 인증 또는 시스템에서 발송하는 이메일을 수신하는 주소입니다.</div>
            </div>
            
            <!-- <div class="sc-juEPzu kOWAuF">
                <div class="wrapper">
                    <div class="title-wrapper">
                        <h3>이메일 수신 설정</h3>
                    </div>
                    
                    <div class="block-for-mobile">
                        <div class="contents">
                            <ul class="sc-hKumaY ciQAsU">
                                <li>
                                    <span>댓글 알림</span>
                                    <div class="sc-fSDTwv bXzMiG">
                                        <div class="circle" style="transform:translate(1.375rem);box-shadow:-2px 0 4px rgba(0, 0, 0, 0.10196078431372549)"></div>
                                    </div>
                                </li>
                                <li>
                                    <span>벨로그 업데이트 소식</span>
                                    <div class="sc-fSDTwv jUnpEn">
                                        <div class="circle" style="transform:translate(0rem);box-shadow:2px 0 4px rgba(0, 0, 0, 0.050980392156862744)"></div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div> -->
            
            <div class="info2-area-withdrawal">
                <div class="wrapper">
                    <div class="title-wrapper">
                        <h3>회원 탈퇴</h3>
                    </div>
                    
                    <div class="block-for-mobile">
                        <div class="contents">
                            <button color="red" class="btn btn-Withdrawal">회원 탈퇴</button>
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
            edting : false,
            input:{
                image: '',                   // 이미지 객체
                imagePath : '',              // db에 저장될 이미지 경로
                uploadReady: false,          // 파일초기화를 위한 변수
                name: '',                    // 사용자 이름 
                blogName : '',               // 블로그 제목
                introduction : '',           // 블로그 소개글 
            } 
        }
    },
    methods:{
        onUploadReady(){
            this.input.uploadReady = true
            // Vue가 다음 DOM 업데이트 사이클에서 요소를 찾을 수 있도록 $nextTick 사용
            this.$nextTick(() => {
                // uploadReady가 true로 변경되었으므로 ref를 사용하여 fileInput에 접근하고 click() 메소드를 호출
                this.$refs.fileInput.click();
            });  
        },
        // 이미지 업로드 실횅
        onUpload() {
            // input을 통해 들온 file 객체를 image 배열에 담는다.
            //this.input.image = this.$refs.fileInput.files
            
            // input에서 선택한 파일을 가져온다.
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
        },

        // 이미지 제거
        imageDelete() {
            
        },

        clearImage() {
            // 이미지 업로드 기능을 비활성화(false)시킨다.
            this.uploadReady = false
        
            // 렌더링 되고 마지막에 실행이 되도록 $nextTick()을 사용한다.
            this.$nextTick(() => {
                this.uploadReady = true
            })
        },

        // 이름, 소개글 수정
        modifyInfo() {
            this.edting = true
            console.log("블로그 정보 수정 버튼이 클릭되었습니다.");
        },

        // 블로그제목 수정
        modifyTitle() {
            
            console.log("벨로그 제목 수정 버튼이 클릭되었습니다.");
        },

        // 이메일 주소 변경
        modifyEmail() {
            
            console.log("이메일 주소 변경 버튼이 클릭되었습니다.");
        },

        // 회원 탈퇴
        Withdrawal() {
            
            console.log("회원 탈퇴 버튼이 클릭되었습니다.");
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
    .edting, .pre-edting{
        flex-direction: column;
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


