
package com.project.emailSchedular.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheetData {
    private String sheetName;
    private List<String> headers;
    private List<Rows> rows;

    @Data
    public static class Rows {
        Map<String,Object> cell;
        private MailResponse mailResponse;
        private String imagePreset;
    }
}
