parameters {
    string(
            description: '输入版本号',
            name: 'version',
            defaultValue: '1.0'
    )
}

def version = "${params.version}"

node {
    stage('git chekout') {
        git branch: 'master', url: 'https://github.com/fastjrun/mytoolbox.git'
    }
    stage('dockerFile') {
        sh 'rm -rf code && mkdir code'
        sh 'cp -r ./app ./code && cp Dockerfile ./code'
        dir('code'){
            stash 'code'
        }
    }
    stage('parallel docker build') {
        parallel (
                'docker build && push arm64': {
                    node('arm64') {
                        dir('workdir'){
                            unstash 'code'
                        }
                        sh 'cd workdir && docker build . -t pi4k8s/mytoolbox:$version-arm64'
                        sh 'docker push pi4k8s/mytoolbox:$version-arm64'
                    }
                },
                'docker build && push amd64': {
                    node('amd64') {
                        dir('workdir'){
                            unstash 'code'
                        }
                        sh 'cd workdir && docker build . -t pi4k8s/mytoolbox:$version-amd64'
                        sh 'docker push pi4k8s/mytoolbox:$version-amd64'
                    }
                }
        )
    }
    stage('manifest'){
        try {
            sh "docker manifest rm pi4k8s/mytoolbox:$version"
        }catch(exc){
            echo "some thing wrong"
        }
        sh "docker manifest create pi4k8s/mytoolbox:$version pi4k8s/mytoolbox:$version-amd64 pi4k8s/mytoolbox:$version-arm64"
        sh "docker manifest annotate pi4k8s/mytoolbox:$version pi4k8s/mytoolbox:$version-amd64 --os linux --arch amd64"
        sh "docker manifest annotate pi4k8s/mytoolbox:$version pi4k8s/mytoolbox:$version-arm64 --os linux --arch arm64"
        sh "docker manifest push pi4k8s/mytoolbox:$version"
    }
}