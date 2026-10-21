package com.example.lecturer_demo.controllers;

import com.example.lecturer_demo.dto.*;
import com.example.lecturer_demo.models.CourseStatus;
import com.example.lecturer_demo.services.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lecturer_demo.dto.PageResponse;

import java.util.List;

@RestController
@RequestMapping("/v1/app/course")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponseV2>>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Sort.Direction direction,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) String keyword) {

        PageResponse<CourseResponseV2> pagedResult = courseService.searchCourses(
                page, size, sortBy, direction, status, keyword);

        ApiResponse<PageResponse<CourseResponseV2>> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Lấy danh sách khóa học thành công");
        apiResponse.setData(pagedResult);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id) {
        try {
            CourseResponse course = courseService.getCourseResponseById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã truy xuất thành công", course));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCourse(@RequestBody CourseCreateRequest request) {
        try {
            courseService.createCourse(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateRequest request) {
        try {
            courseService.updateCourse(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourseById(@PathVariable Long id) {
        try {
            courseService.deleteCourseById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa thành công khóa học", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }
}