package name.panitz.game.framework;
public class TextObject<I> extends AbstractGameObject<I>{
  public String text;
  public String fontName;
  public int fontSize;
  // White Color 1,1,1
  double red = 1;
  double gr = 1;
  double b = 1;
  //
	
  public TextObject(Vertex position, String text, String fntName,int fntSize) {
    super(0,0,position);
    this.text = text ;
    this.fontName = fntName;
    this.fontSize = fntSize;
  }
	
  @Override
  public void paintTo(GraphicsTool<I> g) {
	g.setColor(red, gr, b);
    g.drawString(getPos().x,getPos().y,fontSize,fontName,text);
  }
}

