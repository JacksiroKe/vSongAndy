package com.jackson_siro.visongbook.models;

public class UserModel {
    public Integer userid;
    public String firstname;
    public String lastname;
    public String country;
    public String mobile;
    public Integer gender;
    public String city;
    public String church;
    public String email;
    public String level;
    public String handle;
    public String created;
    public String signedin;
    public String avatarblobid;
    public Integer avatarwidth;
    public Integer avatarheight;
    public Integer points;
    public Integer wallposts;
    public Integer success;
    public String message;
}
//'^users.userid', 'passsalt', 'passcheck' => 'HEX(passcheck)', 'passhash', 'firstname', 'lastname', 'country', 'mobile', 'gender',
// 'city', 'church', 'email', 'level', 'emailcode', 'handle',
//			'created' => 'UNIX_TIMESTAMP(created)', 'sessioncode', 'sessionsource', 'flags', 'signedin' => 'UNIX_TIMESTAMP(signedin)',
//			'signinip', 'written' => 'UNIX_TIMESTAMP(written)', 'writeip',
//			'avatarblobid' => 'BINARY avatarblobid', // cast to BINARY due to MySQL bug which renders it signed in a union
//			'avatarwidth', 'avatarheight', 'points', 'wallposts