//파이어베이스 storage에 추가
/*db.collection("notice").add({
        
    answer: "대답하기 싫습니다."
})
.then(function(docRef) {
    console.log("Document written with ID: ", docRef.id);
})
//에러가 생기면 html에 에러 실행
.catch(function(error) {
    console.error("Error adding document: ", error);
});

//doc("document/문서이름") 
/*const docRef=db.doc("users/nameDoc")
docRef.set({
    first:"a",
    middle:"oh"
});*/
//const { format } = require("path");
//         var i = doc.data().title; << 데이터 저장됨.
//var Title = document.createTextNode("제목 : "+i);

var firebaseConfig = {
    apiKey: "AIzaSyA_ppdr_hCTiN0Uf5SksicWcRxmzuOnLFQ",
    authDomain: "ohaneul-bc2ed.firebaseapp.com",
    databaseURL: "https://ohaneul-bc2ed.firebaseio.com",
    projectId: "ohaneul-bc2ed",
    storageBucket: "ohaneul-bc2ed.appspot.com",
    messagingSenderId: "993527267901",
    appId: "1:993527267901:web:6a79facd864f6981326054",
    measurementId: "G-DN51SYWWDR"
  };
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const db = firebase.firestore();
const storage = firebase.storage();

db.collection('post').doc('CyOmFmrmSusY41SNWcHN').get().then(({ url }) => {
  // `url` is the download URL for 'images/stars.jpg'

  // This can be downloaded directly:
  var xhr = new XMLHttpRequest();
  xhr.responseType = 'blob';
  xhr.onload = function(event) {
    var blob = xhr.response;
  };
  xhr.open('GET', url);
  xhr.send();

  // Or inserted into an <img> element:
  var img = document.getElementById('image_test');
  img.src = url;
}).catch(function(error) {
  console.log('Error getting documents', err);
});


//특정 collection만 데이터만 갖고오기.
/*
  var ref = db.collection('post').doc('YGheXSdMTtu5HMdCr8Bb')
  .get()
      .then(doc => {
         console.log(doc.data());
  

         var test = document.querySelector(".test");
         var image_test = document.querySelector(".image_test");
    
         var pTag = document.createElement( 'p' );
         var pDiv = document.createElement('div');
       
       //  var imagesRef = storageRef.child('images');

         var Title = document.createTextNode("제목 : "+doc.data().title);
         var Date = document.createTextNode("날짜 : "+doc.data().date);
         var Time = document.createTextNode("시간 : "+doc.data().time);
         var UID = document.createTextNode("uid : "+doc.data().uid);
         var URL = document.createTextNode("URL : "+doc.data().url);
         var deleteUrl = document.createTextNode("사진 경로 : "+doc.data().deleteUrl);

         test.appendChild( pTag );
         pDiv.appendChild(URL);
         image_test.appendChild(pDiv);

         pTag.appendChild(Title);
         pTag.appendChild(Date);
         pTag.appendChild(Time);
         pTag.appendChild(UID);
         pTag.appendChild(deleteUrl);
       //  pTag.appendChild(URL);


         
      })
      .catch(err =>{
         console.log('Error getting documents', err);
  });*/


 
  
/*
//전체 문서 읽기
all_data_get();
function all_data_get(){        
  
  db.collection("notify")
        .orderBy("date", "desc")
        .get()
        .then(function(querySnapshot) {    //데이터 읽기 than : 처리해라
          querySnapshot.forEach(function(doc) {
              // doc.data() is never undefined for query doc snapshots
              console.log(doc.id, " => ", doc.data());

              var test = document.querySelector(".test");
              var pTag = document.createElement( 'div' );
              var span = document.createElement('span');
              var pText = document.createTextNode("카테고리 : "+doc.data().category+"\t // \t"); //doc.data().필드이름 data()만 하고 특정 필드 지정을 안하면 Object로 뜸.
              var pDate = document.createTextNode("시간 : "+doc.data().date); 
              var docID = document.createTextNode("문서 아이디 : "+doc.data().docID);
              
              test.appendChild( pTag );
              test.appendChild( span );
              span.appendChild(pText);
              span.appendChild(pDate);
              pTag.appendChild(docID);
          
            });
        });
        }
    /*
function docID_get() {
var docRef = db.collection("post").doc("YGheXSdMTtu5HMdCr8Bb");
docRef.get().then(function(querySnapshot) {
    if (querySnapshot.exists) {
       for(let doc in querySnapshot.data()){

            console.log( `key : ${doc}, value : ${querySnapshot.data()[doc]}` );   
          
       }

    } else {
        // doc.data() will be undefined in this case
        console.log("No such document!");
    }
}).catch(function(error) {
    console.log("Error getting document:", error);
});
  
}

          


/*function data_get(){
  
    //데이터 읽기 than : 처리해라
    db.collection("notify").get().then((querySnapshot) => {
    querySnapshot.forEach((doc) => {
    console.log(`${doc.id} => ${doc.data()}`);
         });
});
}
/*
//핫도그. 
  const docRef = firestore.doc("samples/sndwichData");
  const outputHeader = document.querySelector("#hotDogOutput");
  const inputTextField = document.querySelector("#latestHotDogStatus");
  const saveButton = document.querySelector("#saveButton");
  const loadButton = document.querySelector("#loadButton");

  //버튼을 누르면 컬렉션 필드에 폼에 입력한 문자 추가.
  saveButton.addEventListener ("click", function(){
      const textToSave = inputTextField.value;
      console.log("I am going to save " + textToSave + " to Firestore");
      docRef.set({
          hotDogStatus: textToSave
      }).then(function() {
          console.log("Status saved!");
      }).catch(function (error) {
          console.log("Got an error: ", error);
      });
  });
  //클릭 시 데이터가 문서에 존재하면 내가 입력한 데이터를 갖고온다.
  loadButton.addEventListener("click", function() {
      docRef.get().then(function (doc){
          if(doc && doc.exists) {
              const myData =doc.data();
              outputHeader.innerText = "핫도그 상태: " + myData.hotDogStatus;
          }
      }).catch(function (error) {
          console.log("Got an error", error);
      });
  });
  */
 /*//두 폼에 입력하면 storage로 데이터가 들어감
const form = document.querySelector('#add-cafe-form');
 
form.addEventListener('submit', (e) => {
    e.preventDefault();
    db.collection('notify').add({
        category: form.category.value,
        uid: form.uid.value
    });
    form.category.value = ''; //다 읽고 빈칸으로 채워라.
    form.uid.value = '';
  });*/
