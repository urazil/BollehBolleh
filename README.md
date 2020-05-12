# Bolleh Bolleh ? 작품 소개
```
BollehBolleh는 사용자가 동영상 및 사진들을 공유하는 어플입니다.
YouTube어플을 모티브로 만들었으며 동영상 게시물과 사진 게시물을 보고 댓글도 쓰면서 소통하는 어플입니다.
```

# 개발 환경 
* Android(Kotlin)
* Node.js
* MySQL
* Retrofit2
* AWS(RDS)

# 서버 깃헙

https://github.com/urazil/bollehServer

 # 동영상
 
Bolleh Bolleh App 시연 동영상 입니다.


[![Video Label](https://user-images.githubusercontent.com/48799375/81665852-35577100-947c-11ea-8b83-d168658f1cac.png)](https://youtu.be/EG6hmDY5EtI?t=0s)




# 기능

### 로그인 

<div>
  <img src="https://user-images.githubusercontent.com/48799375/81666949-a77c8580-947d-11ea-9b6e-2abe38404fca.png" width="200"></img>
</div>

아이디와 비밀번호가 맞지않을경우 로그인이 되지않도록 하였습니다.

### 회원가입

<div>
  <img src="https://user-images.githubusercontent.com/48799375/81668116-1e664e00-947f-11ea-8fc7-db24ce5f5d8d.png" width="200"></img>
</div>

 이메일, 비밀번호, 닉네임에 정규식을 사용해서 만들어야 회원가입이 되도록 하였습니다.

### 영상볼래

<div>
  <img src="https://user-images.githubusercontent.com/48799375/81667928-e7903800-947e-11ea-8bdd-c217261d1702.png" width="200"></img>
   <img src="https://user-images.githubusercontent.com/48799375/81668171-3b028600-947f-11ea-9b56-27bce543aaf0.png" width="200"></img>
 <img src="https://user-images.githubusercontent.com/48799375/81668305-72713280-947f-11ea-9942-76f3f4fba094.png" width="200"></img>
  <img src="https://user-images.githubusercontent.com/48799375/81668409-9c2a5980-947f-11ea-8a49-4da6431af2a3.png" width="200"></img>
   <img src="https://user-images.githubusercontent.com/48799375/81670609-d6492a80-9482-11ea-8a8f-bf10d0dd018f.png" width="200"></img>
</div>


영상 썸네일 사진을 원하는 사진으로 지정 할 수 있으며 영상은 MediaPlayer 라이브러리를 사용하였으며 Videocontroller로  영상정지 및 빨리감기 뒤로감기를 구현했습니다. 영상수정,삭제 기능도 구현이 되었습니다. 또한 댓글 기능을 추가하여 사용자끼리 영상에 댓글을 이용하여 소통을 하게끔 만들었습니다.

### 사진볼래

<div>
  <img src="https://user-images.githubusercontent.com/48799375/81669811-b36a4680-9481-11ea-83c1-a86e2a1dacda.png" width="200"></img>
   <img src="https://user-images.githubusercontent.com/48799375/81669859-c5e48000-9481-11ea-90b8-c3afcd3de293.png" width="200"></img>
 <img src="https://user-images.githubusercontent.com/48799375/81669917-dbf24080-9481-11ea-8d93-bf5369616900.png" width="200"></img>
  <img src="https://user-images.githubusercontent.com/48799375/81669967-ef9da700-9481-11ea-81f9-8f1ded896ec6.png" width="200"></img>
</div>

여러장의 사진을 업로드할 수 있으며, 수정 삭제가 가능합니다. Picasso 라이브러리를 사용하여 사진의 크기와 반대로 나오는 현상을 바로 잡았습니다.

### 채팅볼래(구현중)

<div>
  <img src="https://user-images.githubusercontent.com/48799375/81670376-7783b100-9482-11ea-89da-520283bf4802.png" width="200"></img>
   <img src="https://user-images.githubusercontent.com/48799375/81670412-85d1cd00-9482-11ea-816d-65d477c8fede.png" width="200"></img>
</div>

SOCKET.IO를 사용하여 채팅 구현중에 있습니다.

### 내 정보 

<div>
  <img src="https://user-images.githubusercontent.com/48799375/81670869-38099480-9483-11ea-930c-d70a7ed1e3a9.png" width="200"></img>
   <img src="https://user-images.githubusercontent.com/48799375/81671921-6cca1b80-9484-11ea-92d8-afd9e47ce5d5.png" width="200"></img>
   <img src="https://user-images.githubusercontent.com/48799375/81670946-57a0bd00-9483-11ea-8540-110b0d7745cf.png" width="200"></img>
 <img src="https://user-images.githubusercontent.com/48799375/81670984-62f3e880-9483-11ea-8e6c-fce26201da27.png" width="200"></img>
</div>

닉네임 변경과 비밀번호 재설정, 회원 탈퇴 기능이 있습니다.

























