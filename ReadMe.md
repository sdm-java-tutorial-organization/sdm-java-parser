
#HTML PARSER
## html old.parser api document

<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->
<!-- code_chunk_output -->

* [HTML PARSER](#html-old.parser)
	* [html old.parser api document](#html-old.parser-api-document)
		* [환경](#환경)
		* [과정](#과정)
		* [ResourceReader API](#resourcereader-api)
		* [HtmlParser API](#htmlparser-api)
		* [Dom API](#dom-api)

<!-- /code_chunk_output -->

### 들어가기전에

CleanCode와 TestCode를 적용시키려 노력하였다.

### 환경

- Mac OS X
- Java 10.0.1

### 과정

- 문자열검색 - "<"과 ">" 기준으로 검색
- "<" 시작 ">" 종료 문자열이 나온다면 정규식을 통해 올바른 태그인지 확인
	- [주의점1]. 만약 "<script>" 가 나온다면 내부에 문자열이 등장할 수 있음
		- 내부에 문자열이 등장한다면 문자열이 종료될때까지 인식하지 않음
		- JS는 \'로 시작하면 \'로 끝나야 하고, \"로 시작하면 \"로 끝나야함
	- [주의점2]. 만약 "<--" 가 나온다면 주석이 시작되므로 "-->"이 나와 주석이 종료될때까지 인식하지 않음
- 각각의 "<", ">"기준으로 잘려진 문자열배열을 스택과 재귀를 이용해서 Dom구조로 변환

### ResourceReader API

- readFromFile(String src) => String : 파일로부터 문자열얻기
- readFromUrl(String url) => String : URL로부터 문자열얻기

### HtmlParser API

- textToDom(String html) => Dom : 텍스트를 Dom으로 변환해서 최상위 노드를 반환
- domToText(Dom dom) => String : Dom을 텍스트로 변환해서 반환

### Dom API

#### 필드

- boolean isTag : 태그인지 텍스트인지
- String tag : 태그이름
- int tagType : 태그타입 ( 열림인지 닫힘인지 열림과 닫힘이 등시에 있는지 )
- Map<String, String> attrubutes : 태그속성
- Dom[] childs : 하위태그
- String innerText : 텍스트

#### 메소드

- getElementByTagName(String tag) => Dom[] : 태크이름으로 검색
- getElementById(String id) => Dom : 아이디값으로 검색
- getElementsByClassName(String clazz) => Dom[] : 클래스이름으로 검색
- print() => void : 모든태그출력
- printClean() => void : Js & Css & Remark를 제외한 모든태그출력
- toString() => String : String으로 반환
