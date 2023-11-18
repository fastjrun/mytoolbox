import streamlit as st


def main():
    st.title('git更换远程仓库地址')
    input_text = st.text_input('Enter git-url 👇', key='git地址')

    # 实时显示连接字符串
    result_text = 'git remote set-url origin ' + input_text
    st.write('command👇')
    st.code(result_text, language='bash')


if __name__ == '__main__':
    main()
