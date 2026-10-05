<template>
  <!-- 일반 회원 및 점주 회원 선택 버튼 -->
  <div id="regi-tabs">
    <table border="1">
      <tbody>
        <tr>
          <td>
            <button @click="common">일반</button>
          </td>
          <td>
            <button @click="manager">점주</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- 일반 회원 로그인 화면 (일반 버튼 클릭 시 노출) -->
  <div id="regi-common" :style="commonDp">
    <br />
    <h1>일반 회원</h1>
    <br />
    <table border="1">
      <colgroup>
        <col width="100px" />
        <col width="200px" />
        <col width="100px" />
      </colgroup>
      <tbody>
        <!-- 아이디 입력 창 -->
        <tr>
          <th>아이디</th>
          <td>
            <input v-model="id" @input="reBtn" size="20" placeholder="아이디 (필수)" />
            <br />
          </td>
          <td><input type="button" @click="idcheck" value="아이디 중복 확인" /></td>
        </tr>
        <!-- 아이디 중복 문구 -->
        <tr>
          <td colspan="3">
            <p :style="idChk" style="font-size: 13px">{{ idChkStr }}</p>
          </td>
        </tr>
        <!-- 비밀번호 입력 창 -->
        <tr>
          <th>비밀번호</th>
          <td>
            <input type="password" @input="pwRe" v-model="pw" size="20" placeholder="비밀번호 (필수)" />
          </td>
          <td></td>
        </tr>
        <!-- 비밀번호 재확인 창 -->
        <tr>
          <th>비밀번호 재확인</th>
          <td>
            <input type="password" @input="pwRe" v-model="rw" size="20" placeholder="비밀번호 재확인 (필수)" />
          </td>
          <td></td>
        </tr>
        <!-- 비밀번호 재확인 불일치 문구 -->
        <tr>
          <td colspan="3">
            <p :style="pwChk" style="font-size: 13px">{{ pwStr }}</p>
          </td>
        </tr>
        <!-- 비밀번호 생성 조건 -->
        <tr>
          <td colspan="3">
            <ul style="text-align: left; color: #555555">
              <li style="font-size: 11px">7 자리 이상의 알파벳과 숫자로 입력해주세요</li>
              <li style="font-size: 11px">
                대문자, 소문자, 숫자를 최소 1 개씩 포함해야 합니다
              </li>
            </ul>
          </td>
        </tr>
        <!-- 이름 입력 창 -->
        <tr>
          <th>이름</th>
          <td><input v-model="name" size="20" placeholder="이름 (필수)" /></td>
          <td></td>
        </tr>
        <!-- 전화번호 입력 창 -->
        <tr>
          <th>전화번호</th>
          <td>
            <div class="phone-group"> <!-- ✅ 추가: 전화번호 3칸 한 줄 정렬용 -->
              <input v-model="phone1" @input="makePhone" size="1" placeholder="010" maxlength="3" />
              -
              <input v-model="phone2" @input="makePhone" size="2" placeholder="0000" maxlength="4" />
              -
              <input v-model="phone3" @input="makePhone" size="2" placeholder="0000" maxlength="4" />
            </div> <!-- ✅ 추가 -->
          </td>
          <td></td>
        </tr>
        <!-- 이메일 입력 창 -->
        <tr>
          <th>이메일</th>
          <td>
            <input type="email" v-model="email" size="20" placeholder="이메일 (선택)" />
          </td>
          <td></td>
        </tr>
        <!-- 차량번호 입력 창 -->
        <tr>
          <th>차량번호</th>
          <td><input v-model="carNum" size="20" placeholder="전체 차량번호 (필수)" /></td>
          <td></td>
        </tr>

        <!-- 전기차 여부 확인 창(차량 번호 입력 시 노출) -->
        <tr v-if="carNum != ''">
          <td colspan="3">
            <input type="checkbox" v-model="elecBox" @click="elecChk" size="20" />
            <span style="font-size: 13px">전기차일 경우 체크해주세요</span>
          </td>
        </tr>
        <tr>
          <td colspan="3">
            <br /> <!-- ✅ 추가: 여백용 -->
          </td>
        </tr>
        <!-- 회원가입 버튼 -->
        <tr>
          <td colspan="3">
            <button @click="register">회원가입</button>
          </td>
        </tr>
        <!-- 로그인으로 돌아가기 -->
        <tr>
          <td colspan="3">
            <a href="/login">로그인</a>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- 점주 회원 로그인 화면 (점주 버튼 클릭 시 노출) -->
  <div id="regi-manager" :style="managerDp">
    <br />
    <h1>점주 회원</h1>
    <br />
    <table border="1">
      <colgroup>
        <col width="100px" />
        <col width="200px" />
        <col width="100px" />
      </colgroup>
      <tbody>
        <!-- 아이디 입력 창 -->
        <tr>
          <th>아이디</th>
          <td>
            <input v-model="id" size="20" placeholder="아이디 (필수)" />
            <br />
          </td>
          <td><input type="button" @click="idcheck" value="아이디 중복 확인" /></td>
        </tr>
        <!-- 아이디 중복 문구 -->
        <tr>
          <td colspan="3">
            <p :style="idChk" style="font-size: 13px">{{ idChkStr }}</p>
          </td>
        </tr>
        <!-- 비밀번호 입력 창 -->
        <tr>
          <th>비밀번호</th>
          <td>
            <input type="password" @input="pwRe" v-model="pw" size="20" placeholder="비밀번호 (필수)" />
          </td>
          <td></td>
        </tr>
        <!-- 비밀번호 재확인 입력 창 -->
        <tr>
          <th>비밀번호 재확인</th>
          <td>
            <input type="password" @input="pwRe" v-model="rw" size="20" placeholder="비밀번호 재확인 (필수)" />
          </td>
          <td></td>
        </tr>
        <!-- 비밀번호 재확인 불일치 -->
        <tr>
          <td colspan="3">
            <p :style="pwChk" style="font-size: 13px">{{ pwStr }}</p>
          </td>
        </tr>
        <!-- 비밀번호 생성 조건 -->
        <tr>
          <td colspan="3">
            <ul style="text-align: left; color: #555555">
              <li style="font-size: 11px">7 자리 이상의 알파벳과 숫자로 입력해주세요</li>
              <li style="font-size: 11px">
                대문자, 소문자, 숫자를 최소 1 개씩 포함해야 합니다
              </li>
            </ul>
          </td>
        </tr>
        <!-- 이름 입력 창 -->
        <tr>
          <th>이름</th>
          <td><input v-model="name" size="20" placeholder="이름 (필수)" /></td>
          <td></td>
        </tr>
        <!-- 전화번호 입력 창 -->
        <tr>
          <th>전화번호</th>
          <td>
            <div class="phone-group"> <!-- ✅ 추가 -->
              <input v-model="phone1" @input="makePhone" size="1" placeholder="010" maxlength="3" />
              -
              <input v-model="phone2" @input="makePhone" size="2" placeholder="0000" maxlength="4" />
              -
              <input v-model="phone3" @input="makePhone" size="2" placeholder="0000" maxlength="4" />
            </div> <!-- ✅ 추가 -->
          </td>
          <td></td>
        </tr>
        <!-- 이메일 입력 창 -->
        <tr>
          <th>이메일</th>
          <td>
            <input type="email" v-model="email" size="20" placeholder="이메일 (선택)" />
          </td>
          <td></td>
        </tr>
        <!-- 차량번호 입력 창 -->
        <tr>
          <th>차량번호</th>
          <td><input v-model="carNum" size="20" placeholder="전체 차량번호 (필수)" /></td>
          <td></td>
        </tr>
        <!-- 전기차 여부 확인(차량 번호 입력 시 노출) -->
        <tr v-if="carNum != ''">
          <td colspan="3">
            <input type="checkbox" v-model="elecBox" @click="elecChk" size="20" />
            <span style="font-size: 13px">전기차일 경우 체크해주세요</span>
          </td>
        </tr>
        <!-- 점주 확인 코드 입력 창 -->
        <tr>
          <th>점주 확인 코드</th>
          <td>
            <input v-model="mngChk" size="20" placeholder="점주 확인 코드 (필수)" />
          </td>
          <td></td>
        </tr>
        <!-- 회원가입 버튼 -->
        <tr>
          <td colspan="3">
            <button @click="register">회원가입</button>
          </td>
        </tr>
        <!-- 로그인으로 돌아가기 -->
        <tr>
          <td colspan="3">
            <a href="/login">로그인</a>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      commonDp: "display : block;", // 일반 회원 로그인 창 style
      managerDp: "display : none;", // 점주 회원 로그인 창 style

      idChk: "display:none; color:#0000ff;", // 아이디 중복 버튼 문구 style
      idChkStr: "사용할 수 있는 아이디입니다", // 아이디 중복 문구
      iderr: false, // 아이디 중복 검사 결과

      pwChk: "display:none; color:#ff0000;", // 비밀번호 재확인 문구 style
      pwStr: "비밀번호가 일치하지 않습니다", // 비밀번호 재확인 문구
      pwerr: false, // 비밀번호 재확인 검사 결과

      id: "", // 아이디
      pw: "",
      min: 7, // 비밀번호, 비밀번호 검사 문자 길이
      rw: "", // 비밀번호 재확인
      name: "", // 이름
      phone1: "010",
      phone2: "",
      phone3: "",
      phone: "", // 전화번호
      email: "", // 이메일
      carNum: "", // 차량 번호
      carType: 0, // 차량 종류

      elecBox: false, // 전기차 여부 체크박스

      auth: 3, // 1: 관리자 , 2: 점주, 3: 일반 회원

      mngCode: "0000", // 점주 확인 코드
      mngChk: "", // 점주 확인 코드 입력란

      canRegi: false, // 회원가입 검사 결과
    };
  },
  methods: {
    // 일반 회원 버튼 클릭
    common() {
      this.auth = 3; // 권한을 일반 회원으로 설정
      this.managerDp = "display : none;"; // 점주 회원 로그인 창 숨김
      this.commonDp = "display : block"; // 일반 회원 로그인 창 노출
    },

    // 점주 회원 버튼 클릭
    manager() {
      this.auth = 2; // 권한을 점주 회원으로 설정
      this.commonDp = "display : none"; // 일반 회원 로그인 창 숨김
      this.managerDp = "display : block;"; // 점주 회원 로그인 창 노출
    },

    // 아이디 중복 확인 버튼
    idcheck() {
      // 아이디 입력란이 비어있지 않을 경우
      if (this.id !== null && this.id.trim() !== "") {
        // 아이디를 파라미터로 데이터베이스 접근
        axios
          .post("/idcheck", null, { params: { id: this.id } })
          // 접근 성공 시
          .then((resp) => {
            // 접근 확인용 (확인 후 주석)
            // alert(resp.data);

            // 중복되는 아이디가 있을 경우
            if (resp.data === true) {
              this.idChkStr = "사용할 수 없는 아이디입니다"; // 아이디 중복 문구 수정
              this.idChk = "display: block; color:#ff0000;"; // 아이디 중복 문구 노출 (RED)
              this.iderr = false; // 아이디 중복 검사 결과 false
            }
            // 중복되는 아이디가 없을 경우
            else {
              this.idChkStr = "사용할 수 있는 아이디입니다"; // 아이디 중복 문구 수정
              this.idChk = "display: block; color:#0000ff;"; // 아이디 중복 문구 노출 (BLUE)
              this.iderr = true; // 아이디 중복 검사 결과 true
            }
          })
          // 접근 실패 시
          .catch((err) => {
            alert(err);
          });
      }
    },
    // 입력된 아이디가 변경되었을 경우
    reBtn() {
      this.iderr = false; // 아이디 중복 검사 결과를 false 로 전환 -> 중복 검사 재차 필요
    },
    // 비밀번호 문자 검사(비밀번호, 최소 길이)
    pwCheck(str, min) {
      let num = 0; // 숫자의 수
      let sAlp = 0; // 소문자의 수
      let lAlp = 0; // 대문자의 수
      let flag = true; // 비밀번호 문자 검사 결과

      for (let i = 0; i < str.length; i++) {
        // 문자열 길이 검사
        if (str.length >= min) {
          // 숫자, 대문자, 소문자 수량 확인 -> ASCII CODE 이용
          if (str.charAt(i) >= "0" && str.charAt(i) <= "9") {
            num++;
          } else if (str.charAt(i) >= "A" && str.charAt(i) <= "Z") {
            lAlp++;
          } else if (str.charAt(i) >= "a" && str.charAt(i) <= "z") {
            sAlp++;
          }
          // 알파벳과 숫자가 아닌 문자가 포함 시
          else {
            alert("알파벳과 숫자로만 입력해주십시오");
            flag = false; // 검사 결과 false
            return flag; // 결과 리턴
          }
        }
        // 문자 최소 길이 미충족 시
        else {
          alert("최소 " + min + "개 이상의 비밀번호로 입력해주십시오");
          flag = false; // 검사 결과 false
          return flag; // 결과 리턴
        }
      }

      // 최소 길이를 충족하며 대문자 소문자 숫자가 하나씩 포함되었을 경우
      if (lAlp >= 1 && sAlp >= 1 && num >= 1 && str.length >= min) {
        return flag; // 결과 리턴 -> true
      }
      // 상단의 조건에 하나도 부합하지 못할 경우
      else {
        alert("대문자, 소문자, 숫자가 각각 1개씩 포함되어야 합니다");
        flag = false; // 검사 결과 false
        return flag; // 결과 리턴
      }
    },
    // 비밀번호 재확인 검사
    pwRe() {
      // 비밀번호 재확인 입력창이 비어있을 경우
      if (this.rw === null || this.rw.trim() === "") {
        this.pwChk = "display:none; color:#ff0000;"; // 비밀번호 재확인 문구 숨김
        return; // 검사 종료
      }
      // 비밀번호 입력창이 비어있지 않거나 비밀번호와 비밀번호 재확인이 일치하지 않을 경우
      if ((this.pw !== null && this.pw !== "") || this.pw !== this.rw) {
        this.pwChk = "display:block; color:#ff0000;"; // 비밀번호 재확인 문구 노출
        this.pwerr = false; // 비밀번호 재확인 검사 false
      }
      // 비밀번호 입력창이 비어있지 않고, 비밀번호와 비밀번호 재확인이 일치할 경우
      if (this.pw !== null && this.pw !== "" && this.pw === this.rw) {
        this.pwChk = "display:none; color:#ff0000;"; // 비밀번호 재확인 문구 숨김
        this.pwerr = true; // 비밀번호 재확인 검사 true
      }
    },

    // 휴대폰 번호 취합
    makePhone() {
      this.phone = this.phone1 + "-" + this.phone2 + "-" + this.phone3;
      // alert(this.phone);
    },

    // 전기차 여부 클릭
    elecChk() {
      // 체크했을 경우
      if (this.elecBox === false) {
        this.carType = 1; // 전기차 타입으로 변경
        // alert(this.carType);
      }
      // 해제했을 경우
      else {
        this.carType = 0; // 일반 차량으로 변경
        // alert(this.carType);
      }
    },

    // 회원가입 검사
    regiChk() {
      // 회원가입 가능 여부 -> false
      this.canRegi = false;

      // 아이디 미입력일 경우
      if (this.id === null || this.id.trim() === "") {
        alert("아이디를 입력하세요");
        return;
      }

      // 아이디 중복 검사 false 일 경우
      else if (this.iderr === false) {
        alert("이미 존재하는 아이디입니다");
        return;
      }

      // 비밀번호 미입력일 경우
      else if (this.pw === null || this.pw.trim() === "") {
        alert("비밀번호를 입력하세요");
        return;
      }

      // 비밀번호 문자 검사 false 일 경우
      else if (!this.pwCheck(this.pw, this.min)) {
        return;
      }

      // 비밀번호 재확인 미입력일 경우
      else if (this.rw === null || this.rw.trim() === "") {
        alert("비밀번호 재확인을 입력하세요");
        return;
      }

      // 비밀번호 재확인 검사 false 일 경우
      else if (this.pwerr === false) {
        alert("비밀번호가 일치하지 않습니다");
        return;
      }

      // 이름 미입력일 경우
      else if (this.name === null || this.name.trim() === "") {
        alert("이름을 입력하세요");
        return;
      }

      // 전화번호 미입력일 경우
      else if (this.phone2 === null || this.phone2.trim() === "") {
        alert("전화번호를 입력하세요");
        return;
      }

      // 전화번호 미입력일 경우
      else if (this.phone3 === null || this.phone3.trim() === "") {
        alert("전화번호를 입력하세요");
        return;
      }

      // 차량번호 미입력일 경우
      else if (this.carNum === null || this.carNum.trim() === "") {
        alert("차량번호를 입력하세요");
        return;
      }

      // 점주 로그인 시
      if (this.auth === 2) {
        // 점주 승인코드 미입력일 경우
        if (this.mngChk === null || this.mngChk.trim === "") {
          alert("점주 승인 코드를 입력하세요");
          return;
        }
        //점주 승인코드가 일치하지 않을 경우
        else if (this.mngChk !== this.mngCode) {
          alert("점주 승인 코드가 일치하지 않습니다");
          return;
        }
      }
      // 회원가입 검사 결과 -> true
      this.canRegi = true;
    },

    // 회원가입 버튼을 눌렀을 경우
    register() {
      // 회원가입 검사 실행
      this.regiChk();

      // 검사 결과가 true 일 경우
      if (this.canRegi) {
        // 파라미터
        const param = {
          params: {
            id: this.id,
            pw: this.pw,
            name: this.name,
            phone: this.phone,
            email: this.email,
            carNum: this.carNum,
            carType: this.carType,
            auth: this.auth,
          },
        };

        // 회원 정보 데이터 베이스 추가
        axios
          .post("/addmember", null, param)
          // 접근 성공 시
          .then((resp) => {
            // 접근 확인용 (사용 후 주석)
            // alert(resp.data);

            if (resp.data) {
              alert("회원가입에 성공하였습니다"); // 성공 문구 출력
              this.$router.push({ name: "login" }); // 로그인 화면으로 이동
            }
          })
          // 접근 실패 시
          .catch((err) => {
            alert(err);
          });
      }
    },
  },
};
</script>

<style src="@/components/basic/css/common.css"></style>
<style src="@/components/member/css/regi.css"></style>