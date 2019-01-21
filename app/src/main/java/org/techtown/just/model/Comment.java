package org.techtown.just.model;

    public class Comment {
        //json 파싱
        int postId;
        int id;
        String name;
        String email;
        String body;

        public String toString() {
            String s = "";
            s += postId + "/";
            s += id + "/";
            s += name + "/";
            s += email + "/";
            s += body + "/";
            return s;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
