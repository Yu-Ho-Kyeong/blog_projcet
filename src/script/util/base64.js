export default {
    // html에서 img 태그만 추출
    imginHtmlChk(html) {
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, 'text/html');
      const imgTags = doc.querySelectorAll('img');
      let imgSources = [];
  
      if (imgTags.length !== 0) {
        imgTags.forEach(img => {
          imgSources.push(img.src);
        });
        //console.log('imgSources : ' + JSON.stringify(imgSources));
  
        return imgSources;
      } else {
        return false;
      }
    },


    // 이미지 URL을 받아와서 HTML의 이미지 태그의 src 속성을 변경하는 함수
    updateBase64ToServerUrl(html, imgSources, serverImgUrls) {
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, 'text/html');
      const imgTags = doc.querySelectorAll('img');

      imgTags.forEach((img, index) => {
          const base64Src = imgSources[index]; // 현재 img 태그의 base64 소스
          const serverUrl = serverImgUrls[index]; // 서버에서 받아온 이미지 URL
          img.src = img.src.replace(base64Src, serverUrl); // base64 소스를 서버 URL로 교체
      });

      return doc.documentElement.outerHTML; // 변경된 HTML 반환
    },

    // base64 추출
    extractBase64FromImgSrc(imgSrc) {
      const base64String = imgSrc.split(',')[1];
      return base64String;
    },

  }