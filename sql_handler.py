from db_models import SmartcityExpert, SmartcityPatent, SmartcityRequirement, SmartcitySolution, SmartcityCase, \
    SmartcityAchievement, SmartcityInstitution, SmartcityPaper, Relations, UserHistory, UcenterMember


ENTITIES = [SmartcityExpert, SmartcityPatent, SmartcityRequirement, SmartcitySolution, SmartcityCase,
            SmartcityAchievement, SmartcityInstitution, SmartcityPaper]


class SQLHandler(object):

    @staticmethod
    def load_entities():
        res = []
        entity_id2id = {}
        for entity in ENTITIES:
            _entities = entity.query.all()
            for _entity in _entities:
                if not _entity.kg_id:
                    print(_entity)
                    continue
                res.append(_entity.kg_id)
                entity_id2id[_entity.kg_id] = _entity.id
            # print(res)
        return res, entity_id2id

    @staticmethod
    def load_relations():
        res = []
        relation_type2id = {}
        triples = []
        relations = Relations.query.all()
        for relation in relations:
            if relation.head_id != "expe_1" and relation.tail_id != "expe_!":
                relation_type = relation.relation_id.split("_")[0]
                res.append(relation_type)
                relation_type2id[relation_type] = "r" + relation_type
                triples.append([relation.head_id, relation_type, relation.tail_id])
        return list(set(res)), triples, relation_type2id

    @staticmethod
    def load_user_history():
        res = []
        histories = UserHistory.query.all()
        for history in histories:
            res.append(history)
        return res

    @staticmethod
    def load_user_info():
        res = []
        users = UcenterMember.query.all()
        for user in users:
            res.append(user.id)
        return res

    @staticmethod
    def load_path():
        ui = {}
        ii = {}
        histories = SQLHandler.load_user_history()
        for history in histories:
            if history.log_id not in ui.keys():
                ui[history.log_id] = []
            ui[history.log_id].append(history.kg_id.replace("unit", "institution"))
            if history.kg_id.replace("unit", "institution") not in ui:
                ui[history.kg_id.replace("unit", "institution")] = []
            ui[history.kg_id.replace("unit", "institution")].append(history.log_id)

        relations = Relations.query.all()
        for relation in relations:
            if relation.head_id not in ii:
                ii[relation.head_id] = []
            ii[relation.head_id].append(relation.tail_id)
            if relation.tail_id not in ii:
                ii[relation.tail_id] = []
            ii[relation.tail_id].append(relation.head_id)

        return ui, ii


if __name__ == "__main__":
    print(SQLHandler.load_user_history())

