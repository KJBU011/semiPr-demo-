<template>
    <div id="faqlist">
        <h1>FAQ 게시판</h1>
        <h2>자주 묻는 질문을 확인하세요</h2>

        <div>
            <button v-show="login.auth == 1" @click="writeBtn">글쓰기</button>
        </div>

         <div>
            <table v-show="writeDp">
                <colgroup>
                    <col width="100px"/>
                    <col width="600px"/>
                </colgroup>
                <thead>
                    <tr>
                        <th colspan="2">FAQ 작성</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>질문</th>
                        <td><input v-model="wrtitle" placeholder="질문을 입력하세요" size="100"/></td>
                    </tr>
                    <tr>
                        <th>답변</th>
                        <td><input v-model="wrcontent" placeholder="답변을 입력하세요" size="100"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <button @click="writeFaq">작성</button>
                            &nbsp;
                            <button>취소</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        
        <hr/>

        <!-- ✅ 수정: 전체 옵션 추가, placeholder 통일 -->
        <div class="faqsrch">
            <select v-model="category" class="form-select">
                <option value="">선택</option>
                <option v-for="(option, index) in options" v-bind:value="option.value" v-bind:key="index">{{ option.text }}</option>
            </select>
            <input v-model="keyword" placeholder="검색어를 입력하세요" class="form-control" @keyup.enter="searchBtn"/>
            <button @click="searchBtn" class="btn btn-primary">검색</button>
        </div>

        <table>
            <colgroup>
                <col width="100px"/>
                <col width="600px"/>
                <col width="150px"/>
            </colgroup>
            <thead>
                <tr>
                    <th>번호</th><th>제목</th><th>상세보기</th>
                </tr>
            </thead>
            <tbody v-if="faqlist != []" v-for="(faq, index) in faqlist" :key="index">
                <tr>
                    <th>{{ index + ((pageNum - 1) * 10) + 1 }}</th>
                    <td @click="showDown(faq.faqNo)">{{ faq.faqTitle }}</td>
                    <td><button @click="showDown(faq.faqNo)">상세보기</button></td>
                </tr>
                <tr v-show="selectfaq == faq.faqNo">
                    <th>답변</th>
                    <td class="qna-answer">{{ faq.faqContent }}</td>
                    <td v-show="login.auth != 1" class="qna-answer"></td>
                    <td v-show="login.auth == 1" class="qna-answer">
                        <button @click="updateBtn(faq)">수정</button>
                        <button @click="deleteFaq(faq.faqNo)">삭제</button>
                    </td>
                </tr>
            </tbody>
            <tbody v-if="faqlist == ''">
                <tr>
                    <td colspan="3">검색 결과가 없습니다</td>
                </tr>
            </tbody>
        </table>

        <div v-if="faqlist.length != 0" class="pagination-wrap">
            <div class="pagination">
                <button class="page-btn" :disabled="pageNum === 1" @click="pageClick(pageNum - 1)">이전</button>
                <button
                    v-for="n in pages"
                    :key="n"
                    class="page-number"
                    :class="{ active: pageNum === n }"
                    @click="pageClick(n)"
                >{{ n }}</button>
                <button class="page-btn" :disabled="pageNum === pages" @click="pageClick(pageNum + 1)">다음</button>
            </div>
        </div>

        <hr v-if="faqlist.length != 0"/>
        
        <div>
            <table v-show="faqdata != ''">
                <colgroup>
                    <col width="100px"/>
                    <col width="600px"/>
                </colgroup>
                <thead>
                    <tr>
                        <th colspan="2">FAQ 수정</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>질문</th>
                        <td><input v-model="uptitle" :placeholder="faqdata.faqTitle" size="100"/></td>
                    </tr>
                    <tr>
                        <th>답변</th>
                        <td><input v-model="upcontent" :placeholder="faqdata.faqContent" size="100"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <button @click="updateFaq">수정 완료</button>
                            &nbsp;
                            <button @click="cancleBtn">취소</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'Faqlist',
    data() {
        return {
            login:'',
            pageNum: 1,
            faqlist: [],
            // ✅ 수정: 기본값 전체로 변경
            category: '',
            keyword: '',
            options:[
                {text:"제목", value:"faqTitle" },
                {text:"내용", value:"faqContent" },
            ],
            pages: 0,
            showdetail: false,
            selectfaq: null,
            writeDp : false,
            wrtitle : '',
            wrcontent : '',
            faqdata:[],
            uptitle: '',
            upcontent: '',
        }
    },
    mounted () {
        this.getFaqList();
        if(sessionStorage.getItem('login') !== null){
            this.login = JSON.parse(sessionStorage.getItem('login'));
        }
    },
    methods: {
        getFaqList() {
            const param = {
                params:{
                    pageNum : this.pageNum,
                    category : this.category,
                    keyword : this.keyword.trim()
                }
            }
            axios.get('/faqlist', param)
                .then(resp=>{ this.faqlist = resp.data; })
                .catch(err=>{ console.error(err); })
            axios.get('/faqcount', param)
                .then(resp=>{ this.pages = Math.ceil(resp.data / 10) || 1; })
                .catch(err=>{ console.error(err); })
        },
        pageClick(page){ this.pageNum = page; this.getFaqList(); },
        searchBtn(){
            this.pageNum = 1;
            this.getFaqList();
        },
        showDown(faq){ this.selectfaq = faq; this.faqdata = []; this.readFaq(faq); },
        readFaq(faq){
            const param = { params:{ faqNo : faq } }
            axios.get('/readfaq', param)
                .then(resp=>{ })
                .catch(err=>{ console.error(err); })
        },
        writeBtn(){ this.faqdata = []; this.writeDp = true; },
        writeFaq(){
            let checkUpdate = confirm('정말 작성하시겠습니까?');
            if(checkUpdate){
                if(this.wrtitle === null || this.wrtitle.trim() === ""){ alert('제목을 입력해주십시오'); return; }
                if(this.wrcontent === null || this.wrcontent.trim() === ""){ alert('내용을 입력해주십시오'); return; }
                const param = { params:{ faqTitle : this.wrtitle, faqContent : this.wrcontent, id : this.login.id } }
                axios.get('/writefaq', param)
                    .then(resp=>{ alert('작성이 완료되었습니다'); this.writeDp = false; this.wrtitle = ''; this.wrcontent = ''; this.getFaqList(); })
                    .catch(err=>{ console.error(err); });
            }
        },
        updateBtn(faq){ this.faqdata = faq; this.uptitle = faq.faqTitle; this.upcontent = faq.faqContent; this.writeDp = false; this.wrtitle = ''; this.wrcontent = ''; },
        cancleBtn(){ this.faqdata = []; this.uptitle = ''; this.upcontent = ''; },
        updateFaq(){
            let checkUpdate = confirm('정말 수정하시겠습니까?');
            if(checkUpdate){
                if(this.uptitle === null || this.uptitle.trim() === ""){ this.uptitle = this.faqdata.faqTitle; }
                if(this.upcontent === null || this.upcontent.trim() === ""){ this.upcontent = this.faqdata.faqContent; }
                const param = { params:{ faqTitle : this.uptitle, faqContent : this.upcontent, faqNo : this.selectfaq, id : this.login.id } }
                axios.post('/updatefaq', null, param)
                    .then(resp=>{ alert('수정이 완료되었습니다'); this.getFaqList(); this.faqdata = []; this.uptitle = ''; this.upcontent = ''; })
                    .catch(err=>{ console.error(err); });
            }
        },
        deleteFaq(faq){
            let checkDelete = confirm('정말 삭제하시겠습니까?');
            if(checkDelete){
                const param = { params : { faqNo : this.selectfaq } }
                axios.post('/deletefaq', null, param)
                    .then(resp=>{ if(resp.data){ alert('글이 삭제되었습니다'); this.getFaqList();  this.faqdata = []; this.uptitle = ''; this.upcontent = '';} else{ alert('글 삭제에 실패하였습니다') } })
                    .catch(err=>{ console.error(err); })
            }
        },
    },
}
</script>