package io.github.tim12135.demo.controller;

import io.github.tim12135.demo.common.R;
import io.github.tim12135.demo.model.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author tim12135
 * @since 2023/5/4
 */
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    /**
     * Spring security已实现该功能，该接口地址已被filter处理，无法被调用
     *
     * @param loginVO
     * @return
     */
    @Deprecated
    @PostMapping("login")
    public R<String> login(@Valid @RequestBody LoginVO loginVO) {
        return R.ok();
    }

    /**
     * Spring security已实现该功能，该接口地址已被filter处理，无法被调用
     *
     * @return
     */
    @Deprecated
    @PostMapping("logout")
    public R<String> logout() {
        return R.ok();
    }

}
