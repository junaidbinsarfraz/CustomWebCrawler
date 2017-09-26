package googlewebcrawler.util;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/**
 * The class FileUtil is use as a utility to handle the I/O operation on a file
 * 
 * @author Junaid Sarfraz
 */
public class FileUtil {

	/**
	 * The method createDocFile() is use to create a word document using given
	 * paragraphs and keyword
	 * 
	 * @param paragraphs
	 * @param mainKeyToSearch
	 * @return true if successfully created else false
	 */
	public static Boolean createDocFile(List<String> paragraphs, String mainKeyToSearch, List<String> keywords) {

		try {

			XWPFDocument wordDoc = new XWPFDocument();

			XWPFParagraph headingPara = wordDoc.createParagraph();
			headingPara.setAlignment(ParagraphAlignment.CENTER);

			XWPFRun headingRun = headingPara.createRun();
			headingRun.setBold(Boolean.TRUE);
			headingRun.setText(mainKeyToSearch.substring(0, 1).toUpperCase() + mainKeyToSearch.substring(1).toLowerCase());
			headingRun.setFontFamily(Constants.WORD_DOC_FONT_FAMILY_VERDANA);
			headingRun.setFontSize(14);
			headingRun.setUnderline(UnderlinePatterns.SINGLE);
			headingRun.addBreak();
			headingRun.addBreak();
			
			XWPFParagraph keywordsPara = wordDoc.createParagraph();

			XWPFRun keywordsRun = keywordsPara.createRun();
			keywordsRun.setText("Following are the keywords used to search into the web pages");
			keywordsRun.setFontFamily(Constants.WORD_DOC_FONT_FAMILY_VERDANA);
			keywordsRun.setFontSize(12);
			keywordsRun.addBreak();
			
			for(int i = 0; i < keywords.size(); i++) {
				XWPFParagraph keywordPara = wordDoc.createParagraph();

				XWPFRun keywordRun = keywordPara.createRun();
				keywordRun.setItalic(Boolean.TRUE);
				keywordRun.setText((i+1) + ") " +keywords.get(i).substring(0, 1).toUpperCase() + keywords.get(i).substring(1).toLowerCase());
				keywordRun.setFontFamily(Constants.WORD_DOC_FONT_FAMILY_VERDANA);
				keywordRun.setFontSize(12);
			}
			
			XWPFParagraph spacePara = wordDoc.createParagraph();

			XWPFRun spaceRun = spacePara.createRun();
			spaceRun.setText(" ");
			spaceRun.setFontFamily(Constants.WORD_DOC_FONT_FAMILY_VERDANA);
			spaceRun.setFontSize(12);
			spaceRun.addBreak();
			
			XWPFParagraph reportPara = wordDoc.createParagraph();
			reportPara.setAlignment(ParagraphAlignment.CENTER);

			XWPFRun reportRun = reportPara.createRun();
			reportRun.setText("Report");
			reportRun.setBold(Boolean.TRUE);
			reportRun.setUnderline(UnderlinePatterns.SINGLE);
			reportRun.setFontFamily(Constants.WORD_DOC_FONT_FAMILY_VERDANA);
			reportRun.setFontSize(12);
			reportRun.addBreak();

			// For all paragraphs
			for (String paragraph : paragraphs) {
				XWPFParagraph para = wordDoc.createParagraph();

				XWPFRun run = para.createRun();
				
				// Check if paragraph is a link
				if (paragraph.startsWith(Constants.LINK_DETECTION_STRING)) {
					paragraph = paragraph.replace(Constants.LINK_DETECTION_STRING, "");
					run.setText("Source: ");
					appendExternalHyperlink(paragraph, paragraph, para);
					para.createRun().addBreak(BreakType.PAGE);
				} else {
					run.setText(paragraph);
					run.addBreak();
				}
				run.setFontFamily(Constants.WORD_DOC_FONT_FAMILY_VERDANA);
			}

			FileOutputStream out = new FileOutputStream(Constants.OUTPUT_DIR + mainKeyToSearch + ".docx");
			wordDoc.write(out);
			out.close();

		} catch (Exception e) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * Appends an external hyperlink to the paragraph.
	 * 
	 * @param url
	 *            The URL to the external target
	 * @param text
	 *            The linked text
	 * @param paragraph
	 *            the paragraph the link will be appended to.
	 */
	private static void appendExternalHyperlink(String url, String text, XWPFParagraph paragraph) {

		// Add the link as External relationship
		String id = paragraph.getDocument().getPackagePart().addExternalRelationship(url, XWPFRelation.HYPERLINK.getRelation()).getId();

		// Append the link and bind it to the relationship
		CTHyperlink cLink = paragraph.getCTP().addNewHyperlink();
		cLink.setId(id);

		// Create the linked text
		CTText ctText = CTText.Factory.newInstance();
		ctText.setStringValue(text);
		CTR ctr = CTR.Factory.newInstance();
		ctr.setTArray(new CTText[] { ctText });

		// Insert the linked text into the link
		cLink.setRArray(new CTR[] { ctr });
	}

}
