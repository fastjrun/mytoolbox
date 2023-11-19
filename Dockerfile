FROM  pi4k8s/python:3.10

WORKDIR /opt

ADD ./app ./app

ENTRYPOINT streamlit run app/main.py --server.port=8501 --server.enableCORS false --server.enableXsrfProtection false