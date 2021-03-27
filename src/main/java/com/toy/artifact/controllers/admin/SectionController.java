package com.toy.artifact.controllers.admin;


import com.toy.artifact.db.entity.QSection;
import com.toy.artifact.db.service.ForumService;
import com.toy.artifact.db.vo.forum.SectionVo;
import com.toy.artifact.request.WithIdRequest;
import com.toy.artifact.request.admin.forum.SectionEditRequest;
import com.toy.artifact.request.admin.forum.SectionListRequest;
import com.toy.artifact.utils.Paginator;
import com.toy.artifact.utils.RespFormat.JsonError;
import com.toy.artifact.utils.RespFormat.JsonOk;
import com.toy.artifact.utils.RespFormat.JsonResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class SectionController {

    private final ForumService forumService;

    public SectionController(ForumService forumService) {
        this.forumService = forumService;
    }

    @RequestMapping("/section/list")
    public JsonResp<Paginator<SectionVo>> list(@RequestBody SectionListRequest req) {
        QSection qSection = QSection.section;
        var sections = forumService.sectionList()
            .when(req.getName() != null, query -> {
                query.where(qSection.name.like("%"+req.getName()+"%"));
            })
            .paginate(req.getPage(), req.getSize())
            .map(t -> {
                var vo = new SectionVo();
                vo.setId(t.get(qSection.id));
                vo.setName(t.get(qSection.name));
                vo.setDescription(t.get(qSection.description));
                vo.setCreated_at(t.get(qSection.createdAt));
                vo.setStatus(t.get(qSection.status));
                return vo;
            });
        return new JsonOk<>(sections);
    }

    @RequestMapping("/section/edit")
    public JsonResp<Object> edit(@RequestBody SectionEditRequest req) {
        if (req.getId() == null) {
            forumService.sectionCreate(req.getName(), req.getDescription(), req.getStatus());
        } else {
            forumService.sectionUpdate(req.getId(), req.getName(), req.getDescription(), req.getSort());
        }
        return new JsonOk<>(null);
    }

    @RequestMapping("/section/delete")
    public JsonResp<Object> delete(@RequestBody WithIdRequest req) {
        if (req.getId() == null) {
            return new JsonError<>("empty id");
        }
        forumService.sectionDelete(req.getId());
        return new JsonOk<>(null);
    }

}
