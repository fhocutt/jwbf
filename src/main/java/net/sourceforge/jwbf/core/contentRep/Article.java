package net.sourceforge.jwbf.core.contentRep;

import java.util.Date;

import net.sourceforge.jwbf.core.bots.WikiBot;

/**
 * @author Thomas Stock
 */
public class Article implements ArticleMeta, ContentSetable {

  private final WikiBot bot;

  private final SimpleArticle sa;

  private int reload = 0;
  private static final int TEXT_RELOAD = 1 << 1;
  private static final int REVISION_ID_RELOAD = 1 << 2;
  private static final int MINOR_EDIT_RELOAD = 1 << 3;
  private static final int EDITOR_RELOAD = 1 << 4;
  private static final int EDIT_SUM_RELOAD = 1 << 5;
  private static final int EDIT_DATE_RELOAD = 1 << 6;

  private boolean isReload(final int reloadVar) {
    return (reload & reloadVar) == 0;
  }

  private void setReload(final int reloadVar) {
    reload = reload | reloadVar;
  }

  private void unSetReload(final int reloadVar) {
    reload = (reload | reloadVar) ^ reloadVar;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getText() {
    if (isReload(TEXT_RELOAD)) {
      setReload(TEXT_RELOAD);
      setText(bot.readData(sa.getTitle()).getText());
    }
    return sa.getText();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setText(String text) {
    setReload(TEXT_RELOAD);
    sa.setText(text);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRevisionId() {

    if (isReload(REVISION_ID_RELOAD)) {
      setReload(REVISION_ID_RELOAD);
      sa.setRevisionId(bot.readData(sa.getTitle()).getRevisionId());
    }
    return sa.getRevisionId();
  }

  @Override
  public String getEditor() {
    if (isReload(EDITOR_RELOAD)) {
      setReload(EDITOR_RELOAD);
      setEditor(bot.readData(sa.getTitle()).getEditor());
    }
    return sa.getEditor();
  }

  @Override
  public void setEditor(String editor) {
    setReload(EDITOR_RELOAD);
    sa.setEditor(editor);
  }

  @Override
  public String getEditSummary() {
    if (isReload(EDIT_SUM_RELOAD)) {
      setReload(EDIT_SUM_RELOAD);
      setEditSummary(bot.readData(sa.getTitle()).getEditSummary());
    }

    return sa.getEditSummary();
  }

  @Override
  public void setEditSummary(String s) {
    setReload(EDIT_SUM_RELOAD);
    sa.setEditSummary(s);
  }

  @Override
  public boolean isMinorEdit() {
    if (isReload(MINOR_EDIT_RELOAD)) {
      setReload(MINOR_EDIT_RELOAD);
      setMinorEdit(bot.readData(sa.getTitle()).isMinorEdit());
    }
    return sa.isMinorEdit();
  }

  /**
   * @param bot   the
   * @param title of
   */
  public Article(WikiBot bot, String title) {
    this.bot = bot;
    sa = new SimpleArticle(title);
  }

  /**
   * @param bot the
   * @param sa  the
   */
  public Article(WikiBot bot, SimpleArticle sa) {
    this.sa = sa;
    this.bot = bot;
  }

  /**
   * @deprecated use {@link net.sourceforge.jwbf.core.contentRep.Article(java.lang.String)}
   * and {@link #setText(String)} instead.
   */
  @Deprecated
  public Article(WikiBot bot, String text, String title) {
    sa = new SimpleArticle(text, title);
    this.bot = bot;
  }

  /**
   * Save this article.
   */
  public void save() {
    bot.writeContent(sa);
    unSetReload(REVISION_ID_RELOAD);
    setReload(TEXT_RELOAD);
  }

  /**
   * Saves with a given comment.
   */
  public void save(String summary) {
    setEditSummary(summary);
    save();
  }

  /**
   * clear content.
   */
  public void clear() {
    setText("");
    save();
  }

  /**
   * Deletes this article, if the user has the required rights.
   */
  public void delete() {
    bot.delete(sa.getTitle());
  }

  /**
   * @return true if
   * @deprecated do not use this TODO why?
   */
  @Deprecated
  public boolean isEmpty() {
    return getText().length() < 1;
  }

  /**
   * @return the
   * @deprecated do not use this
   */
  @Deprecated
  public WikiBot getBot() {
    return bot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle() {
    // TODO is here a reload mechanism required ?
    return sa.getTitle();
  }

  /**
   * @return the edittimestamp in UTC
   */
  @Override
  public Date getEditTimestamp() {
    if (isReload(EDIT_DATE_RELOAD)) {
      setReload(EDIT_DATE_RELOAD);
      sa.setEditTimestamp(bot.readData(sa.getTitle()).getEditTimestamp());
    }
    return sa.getEditTimestamp();
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated see inheritDoc
   */
  @Override
  @Deprecated
  public boolean isRedirect() {
    return sa.isRedirect();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addText(String text) {
    setText(getText() + text);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTextnl(String text) {
    setText(getText() + "\n" + text);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMinorEdit(boolean minor) {
    sa.setMinorEdit(minor);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTitle(String title) {
    sa.setTitle(title);

  }

  public SimpleArticle getSimpleArticle() {
    return sa;
  }

}
