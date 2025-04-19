package com.student_management.students.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.student_management.students.dto.MonthlyReport;
import com.student_management.students.dto.SuggestionDTO;
import com.student_management.students.model.Expense;
import com.student_management.students.model.User;
import com.student_management.students.repository.ExpenseRepository;
import com.student_management.students.repository.UserRespository;
import com.student_management.students.service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.imageio.event.IIOWriteProgressListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRespository userRespository;

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense, Authentication authentication){
        User user = userRespository.findsByUsername(authentication.getName())
                .orElseThrow();
        expense.setUser(user);
        return ResponseEntity.ok(expenseService.save(expense));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAll(Authentication auth){
        User user = userRespository.findsByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(expenseService.getAllByUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        expenseService.deleteById(id);
        return ResponseEntity.ok("Expense deleted");
    }

    @GetMapping("/monthly-report")
    public ResponseEntity<List<MonthlyReport>> getMonthlyReport(@RequestParam int year, Authentication auth){
        User user = userRespository.findsByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(expenseService.getMonthlyReport(user, year));
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response, Authentication auth) throws IOException{
        User user = userRespository.findsByUsername(auth.getName()).orElseThrow();
        List<Expense> expenses = expenseService.getAllByUser(user);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.csv");

        PrintWriter writer = response.getWriter();
        writer.println("Date,Description,Amount,Category");

        for(Expense e: expenses){
            writer.printf("%s,%s,%.2f,%s%n",
                    e.getDate(),
                    e.getDescription(),
                    e.getAmount(),
                    e.getCategory());
        }

        writer.flush();
        writer.close();
    }

    @GetMapping("/ai-suggestions")
    public ResponseEntity<List<SuggestionDTO>> getAISuggestions(Authentication auth){
        User user = userRespository.findsByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(expenseService.getAISuggestions(user));
    }

    @GetMapping("/exports/pdf")
    public void exportToPDF(HttpServletResponse response, Authentication auth) throws  IOException{
        User user = userRespository.findsByUsername(auth.getName()).orElseThrow();
        List<Expense> expenses = expenseService.getAllByUser(user);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new Document(pdfDoc);


        document.add(new Paragraph("Expense Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20));

        Table table = new Table(4);
        table.addHeaderCell("Date");
        table.addHeaderCell("Description");
        table.addHeaderCell("Amount");
        table.addHeaderCell("Category");

        for(Expense e: expenses){
            table.addCell(e.getDate().toString());
            table.addCell(e.getDescription());
            table.addCell(String.format("%.2f", e.getAmount()));
            table.addCell(e.getCategory().toString());
        }

        document.add(table);
        document.close();

    }


}
