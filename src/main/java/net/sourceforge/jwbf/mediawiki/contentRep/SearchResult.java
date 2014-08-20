/*
 * Copyright 2014 Frances Hocutt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Contributors:
 *   
 */

//cribbing from CategoryItem, replacing pageid with snippet


package net.sourceforge.jwbf.mediawiki.contentRep;

import java.util.Objects;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;

public class SearchResult {

  private final int namespace = 0;
  private final String title = "";
  private final String snippet = "";

  public static final Function<CategoryItem, String> TO_TITLE_STRING_F =
    new Function<CategoryItem, String>() {
    @Nullable
    @Override
    public String apply(@Nullable CategoryItem input) {
      return input.getTitle();
    }
  };

  public SearchResult(String title, int namespace, String snippet) {
    this.title = Preconditions.checkNotNull(title);
    this.namespace = namespace;
    this.snippet = snippet;
  }

  @Override
  public String toString() {
    return com.google.common.base.Objects.toStringHelper(this) //
        .add("title", title) //
        .add("namespace", namespace) //
        .add("snippet", snippet) //
        .toString();
  }

  public String getTitle() {
    return title;
  }

  public int getNamespace() {
    return namespace;
  }

  public int getSnippet() {
    return snippet;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CategoryItem) {
      CategoryItem that = (CategoryItem) obj;
      return Objects.equals(that.getTitle(), this.getTitle()) && //
          Objects.equals(that.getSnippet(), this.getSnippet()) && //
          Objects.equals(that.getNamespace(), this.getNamespace());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, snippet, namespace);
  }
}
