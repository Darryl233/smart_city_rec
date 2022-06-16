from app_base import app
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy(app)


class SmartcityAchievement(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="成就id")
    kg_id = db.Column(db.String(25), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartCityAchievement id:{self.id}, kg_id:{self.kg_id}>"


class SmartcityCase(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="案例id")
    kg_id = db.Column(db.String(20), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartcityCase id:{self.id}, kg_id:{self.kg_id}>"


class SmartcityExpert(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="专家id")
    kg_id = db.Column(db.String(25), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartcityExpert id:{self.id}, kg_id:{self.kg_id}>"


class SmartcityInstitution(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="单位id")
    kg_id = db.Column(db.String(25), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartcityInstitution id:{self.id}, kg_id:{self.kg_id}>"


class SmartcityPaper(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="论文id")
    kg_id = db.Column(db.String(25), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartcityPaper id:{self.id}, kg_id:{self.kg_id}>"


class SmartcityPatent(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="专利id")
    kg_id = db.Column(db.String(25), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartcityPatent id:{self.id}, kg_id:{self.kg_id}>"


class SmartcityRequirement(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="需求id")
    kg_id = db.Column(db.String(20), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartcityRequirement id:{self.id}, kg_id:{self.kg_id}>"


class SmartcitySolution(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="解决方案id")
    kg_id = db.Column(db.String(25), comment="知识图谱id")

    def __repr__(self):
        return f"<SmartcitySolution id:{self.id}, kg_id:{self.kg_id}>"


class Relations(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="关系id")
    relation_id = db.Column(db.String(25), comment=" 该条关系的id")
    head_id = db.Column(db.String(25), comment="头结点id")
    tail_id = db.Column(db.String(25), comment="尾节点id")

    def __repr__(self):
        return f"<Relation id:{self.id}, relation_id: {self.relation_id}, head_id: {self.head_id}, " \
               f"tail_id: {self.tail_id}>"


class UserHistory(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="浏览记录id")
    log_id = db.Column(db.String(19), comment="登录表中id")
    resource_type = db.Column(db.Integer, comment="访问资源类型")
    kg_id = db.Column(db.String(25), comment="知识图谱id")

    def __repr__(self):
        return f"<UserHistory id: {self.id}, log_id: {self.log_id}, resource_type: {self.resource_type}, " \
               f"kg_id: {self.kg_id}"


class UcenterMember(db.Model):
    id = db.Column(db.String(19), primary_key=True, comment="用户id")
    username = db.Column(db.String(50), comment="用户名")

    def __repr__(self):
        return f"<UcenterMember id: {self.id}, username: {self.username}>"
