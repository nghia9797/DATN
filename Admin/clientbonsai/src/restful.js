import axios from 'axios';
export const RestFul =  {
    baseURL: 'http://localhost:8000',

    async get(link, data, params){
        link = this.baseURL + link;
        if(params != null){
            params = new URLSearchParams(params);
            link += "?" + params;
        }
        let res = await axios.get(link)
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async post(link, data){
        link = this.baseURL + link;
        let res = await axios.post(link,data)
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async del(link, params){
       link = this.baseURL + link;
       let res = await axios.delete(link)
       .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async put(link, data){
        link = this.baseURL + link;
        let res = await axios.put(link, data)
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },


    async checkAuth(link, data, params){
        link = this.baseURL + link;
        if(params != null){
            params = new URLSearchParams(params);
            link += "?" + params;
        }
        let res = await axios.get(link,{
            headers: {
                "Authorization":"Bearer "+localStorage.getItem("token")
            }
        })
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async logout(link, data, params){
        link = this.baseURL + link;
        if(params != null){
            params = new URLSearchParams(params);
            link += "?" + params;
        }
        let res = await axios.get(link,{
            headers: {
                "Authorization":"Bearer "+localStorage.getItem("token")
            }
        })
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async getWithToken(link, data, params){
        let config = {
            headers: {
                "Authorization":"Bearer "+localStorage.getItem("token")
            }
        };
        link = this.baseURL + link;
        if(params != null){
            params = new URLSearchParams(params);
            link += "?" + params;
        }
        let res = await axios.get(link, config)
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async downloadWithToken(link, data, params){
        let config = {
            responseType: 'octet/stream',
            headers: {
                "Authorization":"Bearer "+localStorage.getItem("token")
            }
        };
        link = this.baseURL + link;
        if(params != null){
            params = new URLSearchParams(params);
            link += "?" + params;
        }
        let res = await axios.get(link, config)
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            let blob = new Blob([res],{
                type: "octet/stream"
            });
            let u = URL.createObjectURL(blob);
            window.open(u,"_blank");
        }
    },

    async postWithToken(link, data){
        let config = {
            headers: {
                "Authorization":"Bearer "+localStorage.getItem("token")
            }
        };
        link = this.baseURL + link;
        let res = await axios.post(link,data,config)
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async delWithToken(link, params){
        let config = {
            headers: {
                "Authorization":"Bearer "+localStorage.getItem("token")
            }
        };
       link = this.baseURL + link;
       let res = await axios.delete(link, config)
       .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },

    async putWithToken(link, data){
        let config = {
            headers: {
                "Authorization":"Bearer "+localStorage.getItem("token")
            }
        };
        link = this.baseURL + link;
        let res = await axios.put(link, data, config)
        .catch(e => {
            console.log(e);
            return null;
        });
        if(res != null){
            return res.data;
        }else return null;
    },
}
