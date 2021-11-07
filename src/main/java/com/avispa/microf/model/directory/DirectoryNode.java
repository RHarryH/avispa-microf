package com.avispa.microf.model.directory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DirectoryNode {
    private String id;
    private String parent;
    private String text;
    private String href;
    private String type;
}