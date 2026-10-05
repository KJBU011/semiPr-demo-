<template>
    <div>
        
    </div>
</template>

<script>
export default {
    
    data() {
        return {
            currentMenu: "list", // 기본으로 보여줄 메뉴
        };
    },
    mounted() {
        // ★ 아래 3줄 추가: nav.vue 드롭다운에서 ?menu=boardlist 같은 쿼리로 넘어온 경우
        //   그 메뉴를 기본값으로 사용
        if (this.$route.query.menu) {
            this.currentMenu = this.$route.query.menu;
        }

        if(this.currentMenu === 'list'){
                this.$router.push({name:'boardlist'});
            } else if(this.currentMenu === 'faq'){
                this.$router.push({name:'faqboard'});
            } else if(this.currentMenu === 'qna'){
                this.$router.push({name:'qna'});
            }
    },
    // ★ watch 블록 전체 추가: 이미 /manager 페이지에 있는 상태에서
    //   nav 드롭다운으로 다시 메뉴를 클릭했을 때 반영하기 위함.
    //   '$route.query.menu'처럼 문자열 경로로 watch하면 라우터 설정에 따라
    //   안 걸리는 경우가 있어서, $route 객체 전체를 watch하는 방식으로 함
    watch: {
        '$route'(to) {
            if (to.query.menu) {
                this.currentMenu = to.query.menu;
                if(this.currentMenu === 'list'){
                    this.$router.push({name:'boardlist'});
                } else if(this.currentMenu === 'faq'){
                    this.$router.push({name:'faqboard'});
                } else if(this.currentMenu === 'qna'){
                    this.$router.push({name:'qna'});
            }
            }
        }
    },
}
</script>