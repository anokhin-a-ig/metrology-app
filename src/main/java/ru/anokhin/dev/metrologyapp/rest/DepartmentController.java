package ru.anokhin.dev.metrologyapp.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anokhin.dev.metrologyapp.dto.entity.DepartmentDto;
import ru.anokhin.dev.metrologyapp.dto.request.DepartmentRequestDto;
import ru.anokhin.dev.metrologyapp.dto.response.DepartmentResponseDto;
import ru.anokhin.dev.metrologyapp.service.dto.DepartmentDtoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Контроллер департаментов", description = "CRUD операции с департаментами")
public class DepartmentController {

    private final DepartmentDtoService departmentDtoService;

    public DepartmentController(DepartmentDtoService departmentDtoService) {
        this.departmentDtoService = departmentDtoService;
    }

    @Operation(
            summary = "Добавление департамента",
            description = "Создает новый департамент в системе. " +
                    "Принимает название, тип подразделения и (опционально) идентификатор родительского департамента. " +
                    "Возвращает созданный департамент с присвоенным идентификатором." +
                    "Варианты \"type:\" FACTORY, WORKSHOP, AREA, LABORATORY"
    )
    @ApiResponse(responseCode = "201", description = "Департамент успешно создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
    @PostMapping
    public ResponseEntity<DepartmentResponseDto> create(@RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto created = departmentDtoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Получение списка всех департаментов",
            description = "Возвращает список всех департаментов в системе"
    )
    @ApiResponse(responseCode = "200", description = "Список департаментов успешно получен")
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> findAll() {
        List<DepartmentResponseDto> depList = departmentDtoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(depList);
    }

    @Deprecated
    @GetMapping(params = {"offset", "limit"})
    public ResponseEntity<List<DepartmentResponseDto>> findAllWithPagination(
            @RequestParam Integer offset, @RequestParam Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).body(departmentDtoService.findAllWithPagination(offset, limit));
    }

    @Operation(
            summary = "Получение списка всех департаментов с постраничным выводом",
            description = "Возвращает список всех департаментов в системе с постраничным выводом"
    )
    @ApiResponse(responseCode = "200", description = "Список департаментов успешно получен")
    @GetMapping(path = "/all-by-type", params = {"page", "size", "type", "sortBy"})
    public ResponseEntity<Page<DepartmentResponseDto>> findAllWithPageable(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String type,
            @RequestParam String sortBy) {
        return ResponseEntity.ok(departmentDtoService.findAllWithPageable(page, size, type, sortBy));
    }

    @Operation(
            summary = "Получение дочерних департаментов с постраничным выводом",
            description = "Возвращает дочерние департаменты указанного родителя с постраничным выводом"
    )
    @ApiResponse(responseCode = "200", description = "Список дочерних департаментов успешно получен")
    @GetMapping(path = "/by-parent", params = {"parentId", "page", "size", "sortBy"})
    public ResponseEntity<Page<DepartmentResponseDto>> findByParent(
            @Parameter(description = "Идентификатор родительского департамента", required = true, example = "1")
            @RequestParam Long parentId,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok(departmentDtoService.findByParent(parentId, page, size, sortBy));
    }

    @Operation(
            summary = "Поиск департаментов по части названия",
            description = "Возвращает департаменты, название которых содержит указанный текст без учёта регистра"
    )
    @ApiResponse(responseCode = "200", description = "Результаты поиска получены")
    @GetMapping(path = "/search-by-name", params = {"search"})
    public ResponseEntity<List<DepartmentDto>> searchByName(@RequestParam String search) {
        return ResponseEntity.ok(departmentDtoService.searchByName(search));
    }


    @Operation(
            summary = "Получение департамента по идентификатору",
            description = "Возвращает департамент по указанному идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Департамент успешно получен")
    @ApiResponse(responseCode = "404", description = "Департамент не найден")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> findById(
            @Parameter(description = "Идентификатор департамента", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                departmentDtoService.findById(id)
        );
    }

    @Operation(
            summary = "Получение департамента по имени",
            description = "Возвращает департамент по указанному имени"
    )
    @ApiResponse(responseCode = "200", description = "Департамент успешно получен")
    @ApiResponse(responseCode = "404", description = "Департамент не найден")
    @GetMapping(params = "name")
    public ResponseEntity<DepartmentResponseDto> findByName(
            @Parameter(description = "Имя департамента", required = true, example = "Отдел главного метролога")
            @RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(
                departmentDtoService.findByName(name)
        );
    }

    @GetMapping(params = "type")
    public ResponseEntity<List<DepartmentResponseDto>> findByType(@RequestParam String type) {
        return ResponseEntity.status(HttpStatus.OK).body(
                departmentDtoService.findByType(type)
        );
    }

    @Operation(
            summary = "Обновление департамента",
            description = "Обновляет данные департамента по указанному идентификатору. " +
                    "Принимает название, тип подразделения и (опционально) идентификатор родительского департамента."
    )
    @ApiResponse(responseCode = "200", description = "Департамент успешно обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
    @ApiResponse(responseCode = "404", description = "Департамент не найден")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> changeById(
            @Parameter(description = "Идентификатор департамента", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody DepartmentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(departmentDtoService.changeById(id, dto));
    }

    @Operation(
            summary = "Удаление департамента",
            description = "Удаляет департамент по указанному идентификатору" +
                    "Если удаляемый департамент имеет дочерние (зависимые) департаменты, то сначала необходимо" +
                    "либо удалить все зависимые департаменты, либо переназначить их на другой родительский департамент"
    )
    @ApiResponse(responseCode = "200", description = "Департамент успешно удален")
    @ApiResponse(responseCode = "400", description = "Департамент не найден или имеет зависимые подразделения")
    @ApiResponse(responseCode = "500", description = "Удалите или переназначьте сначала зависимые департаменты")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "Идентификатор департамента", required = true, example = "1")
            @PathVariable Long id) {
        departmentDtoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("123");
    }
}