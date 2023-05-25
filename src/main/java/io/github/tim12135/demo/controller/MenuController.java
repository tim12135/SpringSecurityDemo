package io.github.tim12135.demo.controller;

import io.github.tim12135.demo.common.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author tim12135
 * @since 2023/5/25
 */
@RestController
public class MenuController {

    @PreAuthorize("hasAnyAuthority('sys.menu.view')")
    @GetMapping("/menu/view")
    public R<List<String>> view() {
        return R.ok(Arrays.asList("a", "b"));
    }

    @PreAuthorize("hasAnyAuthority('sys.menu.add')")
    @GetMapping("/menu/add")
    public R<String> add() {
        return R.ok("c");
    }
}
