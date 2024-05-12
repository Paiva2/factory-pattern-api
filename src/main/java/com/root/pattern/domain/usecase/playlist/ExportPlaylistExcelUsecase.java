package com.root.pattern.domain.usecase.playlist;

import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.entity.PlaylistMusic;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Builder
@AllArgsConstructor
public class ExportPlaylistExcelUsecase {
    private final static byte[] PURPLE_CUSTOM_COLOR = new byte[]{100, 72, (byte) 171};
    private final static String[] HEADER_ROW_TITLES = new String[]{"Musician", "Name", "Is a single", "Duration", "Album", "Created at"};
    private final static Integer INITIAL_ROW_OF_TITLES_HEADERS = 4;
    private final static Integer INITIAL_ROW_OF_VALUES = 5;
    private final static String DEFAULT_LOGO_URL = "https://welcon.kocca.kr/cmm/getImage.do?atchFileId=FILE_1378953d-22d1-482b-8e8d-9f40d97bba0f&amp;fileSn=1&amp;thumb=";

    private final PlaylistDataProvider playlistDataProvider;

    public ByteArrayResource exec(UUID playlistId) {
        try {
            ByteArrayOutputStream spreadsheetStream = new ByteArrayOutputStream();

            this.validateInputs(playlistId);

            Playlist playlist = this.checkIfPlaylistExists(playlistId);
            this.checkIfPlaylistIsDisabled(playlist);

            XSSFWorkbook workbook = this.createWorkbook();
            Sheet sheet = this.createSheet(workbook, playlist.getName());

            Row headersRows = sheet.createRow(INITIAL_ROW_OF_TITLES_HEADERS);

            CellStyle headerCellStyles = this.handleCellStyle(sheet, workbook);

            this.handleLogoPicture(workbook, sheet, playlist.getCoverImage());

            for (int i = 0; i <= HEADER_ROW_TITLES.length - 1; i++) {
                Cell headerCell = headersRows.createCell(i);

                headerCell.setCellValue(HEADER_ROW_TITLES[i]);
                headerCell.setCellStyle(headerCellStyles);

                sheet.autoSizeColumn(i);
            }

            List<PlaylistMusic> musics = new ArrayList<>(this.getPlaylistMusics(playlist));
            SimpleDateFormat dateFormatter = this.dateFormatter("yyyy/MM/dd");

            for (int i = 0; i <= playlist.getPlaylistMusics().size() - 1; i++) {
                Row row = sheet.createRow(INITIAL_ROW_OF_VALUES + i);
                PlaylistMusic musicOnPlaylist = musics.get(i);

                Music music = musicOnPlaylist.getMusic();
                String musicDuration = this.handleMusicDuration(music.getDuration());

                row.createCell(0).setCellValue(music.getMusician().getName());
                row.createCell(1).setCellValue(music.getName());
                row.createCell(2).setCellValue(music.getIsSingle());
                row.createCell(3).setCellValue(musicDuration);
                row.createCell(4).setCellValue(music.getAlbum().getName());
                row.createCell(5).setCellValue(dateFormatter.format(music.getCreatedAt()));
            }

            workbook.write(spreadsheetStream);
            workbook.close();

            return new ByteArrayResource(spreadsheetStream.toByteArray());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            throw new BadRequestException("Error while generating exportable data...");
        }
    }

    private void validateInputs(UUID playlistId) {
        if (Objects.isNull(playlistId)) {
            throw new BadRequestException("Playlist id can't be empty");
        }
    }

    private Playlist checkIfPlaylistExists(UUID playlistId) {
        return this.playlistDataProvider.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist"));
    }

    private void checkIfPlaylistIsDisabled(Playlist playlist) {
        if (playlist.isDisabled()) {
            throw new ForbiddenException("Playlist is disabled");
        }
    }

    private XSSFWorkbook createWorkbook() {
        return new XSSFWorkbook();
    }

    private Sheet createSheet(XSSFWorkbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    private CellStyle handleCellStyle(Sheet sheet, XSSFWorkbook workbook) {
        CellStyle style = sheet.getWorkbook().createCellStyle();

        XSSFColor customColor = this.createCustomColor(PURPLE_CUSTOM_COLOR);

        style.setFillBackgroundColor(customColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font fontStyle = this.handleFontStyle(workbook);
        style.setFont(fontStyle);

        return style;
    }

    private XSSFColor createCustomColor(byte[] customRgbColor) {
        XSSFColor customColor = new XSSFColor(customRgbColor, null);

        return customColor;
    }

    private Font handleFontStyle(XSSFWorkbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.index);
        headerFont.setFontName("Sans Serif");

        return headerFont;
    }

    private void handleLogoPicture(XSSFWorkbook workbook, Sheet sheet, String coverImage) {
        String coverImageOnSpreadsheet;

        if (Objects.nonNull(coverImage)) {
            coverImageOnSpreadsheet = coverImage;
        } else {
            coverImageOnSpreadsheet = DEFAULT_LOGO_URL;
        }

        try {
            URLConnection httpCon = new URL(coverImageOnSpreadsheet).openConnection();
            httpCon.addRequestProperty("User-Agent", "Mozilla/4.0");

            InputStream is = httpCon.getInputStream();
            byte[] imageBytes = IOUtils.toByteArray(is);
            is.close();

            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

            XSSFCreationHelper helper = workbook.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = helper.createClientAnchor();

            this.setLogoPosition(anchor);

            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(5, 3.8);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            throw new BadRequestException("Error while converting logo to bytes...");
        }
    }

    private void setLogoPosition(XSSFClientAnchor anchor) {
        anchor.setRow1(0);
        anchor.setRow2(1);
        anchor.setCol1(0);
        anchor.setCol2(1);
    }

    private Set<PlaylistMusic> getPlaylistMusics(Playlist playlist) {
        return playlist.getPlaylistMusics();
    }

    private SimpleDateFormat dateFormatter(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    private String handleMusicDuration(Long duration) {
        long seconds = duration % 60;
        long hours = duration / 60;
        long minutes = hours % 60;
        hours = hours / 60;

        StringBuilder stringBuilderDuration = new StringBuilder();

        if (hours > 0) {
            stringBuilderDuration.append(hours).append(":");
        }

        stringBuilderDuration.append(minutes).append(":").append(seconds);

        return stringBuilderDuration.toString();
    }
}
