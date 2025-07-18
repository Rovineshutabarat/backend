package com.chelly.backend.controller;

import com.chelly.backend.handler.ResponseHandler;
import com.chelly.backend.models.Wallet;
import com.chelly.backend.models.payload.request.WalletRequest;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
@Tag(name = "Rekening Bank", description = "API untuk mengelola rekening bank pengguna")
public class WalletController {
    private WalletService walletService;

    @Operation(summary = "Mendapatkan daftar rekening bank pengguna saat ini",
            description = "Mengambil semua rekening bank yang terhubung dengan pengguna yang sedang login.")
    @GetMapping("/current-user")
    public ResponseEntity<SuccessResponse<List<Wallet>>> getCurrentUserBankAccounts() {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Daftar rekening bank berhasil diambil.",
                walletService.getCurrentUserBankAccounts()
        );
    }

    @Operation(summary = "Mencari rekening bank berdasarkan ID",
            description = "Mengambil detail rekening bank berdasarkan ID yang diberikan.")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Wallet>> findBankAccountById(
            @Parameter(description = "ID rekening bank yang akan dicari", required = true)
            @PathVariable Integer id) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Rekening bank berhasil ditemukan.",
                walletService.findBankAccountById(id)
        );
    }

    @Operation(summary = "Membuat rekening bank baru",
            description = "Menambahkan rekening bank baru untuk pengguna yang sedang login.")
    @PostMapping
    public ResponseEntity<SuccessResponse<Wallet>> createBankAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data rekening bank baru", required = true,
                    content = @Content(schema = @Schema(implementation = WalletRequest.class)))
            @RequestBody @Valid WalletRequest walletRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.CREATED,
                "Rekening bank berhasil dibuat.",
                walletService.createBankAccount(walletRequest)
        );
    }

    @Operation(summary = "Memperbarui rekening bank",
            description = "Memperbarui detail rekening bank yang sudah ada. Hanya pemilik rekening atau admin yang dapat memperbarui.")
    @PutMapping("/{id}")
    @PreAuthorize("@walletService.canModifyBankAccount(#id)")
    public ResponseEntity<SuccessResponse<Wallet>> updateBankAccount(
            @Parameter(description = "ID rekening bank yang akan diperbarui", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data rekening bank yang diperbarui", required = true,
                    content = @Content(schema = @Schema(implementation = WalletRequest.class)))
            @RequestBody @Valid WalletRequest walletRequest) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Rekening bank berhasil diperbarui.",
                walletService.updateBankAccount(id, walletRequest)
        );
    }

    @Operation(summary = "Mengatur rekening bank sebagai default",
            description = "Mengatur rekening bank tertentu sebagai rekening default untuk pengguna. Ini akan mengatur rekening default lainnya menjadi non-default.")
    @PutMapping("/default/{id}")
    @PreAuthorize("@walletService.canModifyBankAccount(#id)")
    public ResponseEntity<SuccessResponse<Wallet>> setDefaultBankAccount(
            @Parameter(description = "ID rekening bank yang akan dijadikan default", required = true)
            @PathVariable Integer id) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Rekening bank berhasil diatur sebagai default.",
                walletService.setDefaultBankAccount(id)
        );
    }

    @Operation(summary = "Menghapus rekening bank",
            description = "Menghapus rekening bank berdasarkan ID. Hanya pemilik rekening atau admin yang dapat menghapus.")
    @DeleteMapping("/{id}")
    @PreAuthorize("@walletService.canModifyBankAccount(#id)")
    public ResponseEntity<SuccessResponse<Wallet>> deleteBankAccount(
            @Parameter(description = "ID rekening bank yang akan dihapus", required = true)
            @PathVariable Integer id) {
        return ResponseHandler.buildSuccessResponse(
                HttpStatus.OK,
                "Rekening bank berhasil dihapus.",
                walletService.deleteBankAccount(id)
        );
    }
}