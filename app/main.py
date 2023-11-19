import streamlit as st


def main():
    st.title('我的工具箱')
    tabs = st.tabs(["git更换远程仓库地址", "查看容器实时日志，从最后指定行行开始"])
    with tabs[0]:
        input_text = st.text_input('Enter git-url 👇', key='git_url', value="https://gitee.com/fastjrun/hello.git")

        result_text = 'git remote set-url origin ' + input_text
        st.write('command👇')
        st.code(result_text, language='bash')

    with tabs[1]:
        with st.container():
            col1, col2 = st.columns(2)
            with col1:
                container_name_text = st.text_input('输入容器名称 👇', key='container_name', value='redis')
            with col2:
                tails_number = st.text_input('输入指定行 👇', key='tails_number', value='20')
        result_text = 'docker logs -f --tail= ' + tails_number + ' ' + container_name_text
        st.write('command👇')
        st.code(result_text, language='bash')


if __name__ == '__main__':
    main()
